package com.alejandro.OpenEarth.security;

import com.alejandro.OpenEarth.serviceImpl.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component("jwtAuthFilter")
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "/api/house/categories",
            "/api/house/details",
            "/api/user/details",
            "/api/house",
            "/api/house/status",
            "/api/house/countries",
            "/api/house/nearTo",
            "/api/house/owner",
            "/api/rent/house",
            "/api/chat/upload-attachment",
            "/api/chat/audio/",
            "/api/chat/chat.markRead",
            "/api/chat/chat.sendMessage"
    );

    // If happens any error, this function is triggered to send token related errors.
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.resetBuffer();
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format("{\"error\":\"%s\",\"message\":\"%s\"}",
                    status.getReasonPhrase(),
                    message.replace("\"", "\\\""));

            response.getOutputStream().write(jsonResponse.getBytes("UTF-8"));
            response.getOutputStream().flush();
            response.flushBuffer();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // routes that should not been filtered by JwtService
        return path.startsWith("/api/auth/") || path.startsWith("/api/geo/") || path.startsWith("/api/picture/") || path.startsWith("/ws/chat") || ALLOWED_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null) {
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "No token on authorization header");
                return;
            }

            if (!authHeader.startsWith("Bearer ")) {
                sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Invalid token format");
                return;
            }

            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Empty token");
                return;
            }

            String userEmail;
            try {
                userEmail = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException eje) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT token is expired: " + eje.getMessage());
                return;
            } catch (MalformedJwtException | SignatureException mjese) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT token is invalid: " + mjese.getMessage());
                return;
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Authentication error: " + e.getMessage());
        }
    }

}

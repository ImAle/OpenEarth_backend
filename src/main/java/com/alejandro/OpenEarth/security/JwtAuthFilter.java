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
            "/api/house/countries"
    );

    // If happens any error, this function is triggered to send token related errors.
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponse = String.format("{ \"error\": \"%s\", \"message\": \"%s\" }", status.getReasonPhrase(), message);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // routes that should not been filtered by JwtService
        return path.startsWith("/api/auth/") || path.startsWith("/api/geo/") || ALLOWED_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null) {
                throw new ServletException("No token on authorization header");
            }

            if (!authHeader.startsWith("Bearer ")) {
                throw new ServletException("Invalid token format");
            }

            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                throw new MalformedJwtException("Empty token");
            }

            String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException eje) {
            sendErrorResponse(response,
                    HttpStatus.UNAUTHORIZED,
                    "JWT token is expired: " + eje.getMessage()
            );
        }catch (MalformedJwtException | SignatureException mjese) {
            sendErrorResponse(response,
                    HttpStatus.UNAUTHORIZED,
                    "JWT token is invalid: " + mjese.getMessage()
            );
        } catch (ServletException se) {
            sendErrorResponse(response,
                    HttpStatus.BAD_REQUEST,
                    "Authentication Error: " + se.getMessage()
            );
        }
    }

}

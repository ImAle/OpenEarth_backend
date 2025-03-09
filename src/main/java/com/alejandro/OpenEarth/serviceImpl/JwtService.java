package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service("jwtService")
public class JwtService {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    private static final long EXPIRATION_HOURS = 48;
    private static final long EXPIRATION = EXPIRATION_HOURS * 60 * 60 * 1000;

    // HS512 secret key
    private final String secretString = "42971&$(384€##81MoreSecretThanAnythingBeforeYouCouldHaveEverSeen838)539892085932@##€#@Secret?Yeah, pretty much secret";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey)
                .compact();
    }
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public User getUser(String token) {
        token = token.replace("Bearer ","");
        return userService.getUserByEmail(extractUsername(token));
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean isAdmin(String token){
        boolean respuesta = false;

        token = token.replace("Bearer ","");

        if (getUser(token).getRole().equals("ROLE_ADMIN"))
            respuesta = true;

        return respuesta;
    }

    public boolean isGuest(String token){
        boolean respuesta = false;

        token = token.replace("Bearer ","");

        if (getUser(token).getRole().equals("ROLE_GUEST"))
            respuesta = true;

        return respuesta;
    }

    public boolean isHostess(String token){
        boolean respuesta = false;

        token = token.replace("Bearer ","");

        if (getUser(token).getRole().equals("ROLE_HOSTESS"))
            respuesta = true;

        return respuesta;
    }
}

package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.UserCreationDto;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.exception.UserNotEnabledException;
import com.alejandro.OpenEarth.serviceImpl.AuthService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    private AuthService authService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password){
        try{
            String token = authService.login(email, password);
            User user = jwtService.getUser(token);

            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "id", user.getId(),
                    "username", jwtService.extractUsername(token),
                    "role", user.getRole()
            ));

        }catch(UserNotEnabledException unex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("Error", unex.getMessage()));
        }catch (AuthenticationException aex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", "Invalid credentials"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreationDto userDto, BindingResult result){
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            String token = authService.register(userDto).getFirst();
            return ResponseEntity.ok().body(Map.of("token", token));
        }catch(RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtex.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(Map.of("role", jwtService.getUser(token).getRole()));
    }

    @PostMapping("/requestReset")
    public ResponseEntity<?> requestReset(@RequestParam("email") String email){
        try{
            User user = userService.getUserByEmail(email);
            authService.setTokenforPasswordReset(user);

            return ResponseEntity.ok().body(Map.of("message", "Check your email for the link to reset your password"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token){
        try{
            User user = jwtService.getUser(token);

            if(!jwtService.isTokenValid(token, user))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));

            return ResponseEntity.ok().body(Map.of("message", "Your token is valid"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String token, @RequestParam("newPassword") String newPassword){
        try{
            User user = jwtService.getUser(token);
            authService.updateUserPassword(user, newPassword);

            return ResponseEntity.ok().body(Map.of("message", "Your password has been reset"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

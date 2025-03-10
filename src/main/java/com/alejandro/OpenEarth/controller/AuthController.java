package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password){
        try{
            String token = authService.login(username, password);
            return ResponseEntity.ok().body(Map.of("token", token));
        }catch (AuthenticationException aex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /*
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        try{
            String token = authService.register(user);
        }
    }*/
}

package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.UserCreationDto;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.AuthService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
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
    @Qualifier("jwtService")
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password){
        try{
            String token = authService.login(email, password);
            return ResponseEntity.ok().body(Map.of("token", token, "id", jwtService.getUser(token).getId()));
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

}

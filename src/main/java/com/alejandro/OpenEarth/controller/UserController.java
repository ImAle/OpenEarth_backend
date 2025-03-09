package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam("id") long userId){
        try{
            userService.activateUserById(userId);
            return ResponseEntity.ok().body(Map.of("message", "The user has been successfully activated"));
        }catch (UsernameNotFoundException unfex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", unfex.getMessage()));
        }catch(RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Error", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(@RequestParam("id") long userId){
        try{
            userService.deactivateUserById(userId);
            return ResponseEntity.ok().body(Map.of("message", "The user has been successfully deactivated"));
        }catch (UsernameNotFoundException unfex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", unfex.getMessage()));
        }catch(RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Error", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        try{
            return ResponseEntity.ok().body(Map.of("users", userService.getUsers()));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", rtex.getMessage()));
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam("id") long userId){
        try{
            return ResponseEntity.ok().body(Map.of("user", userService.getUserById(userId)));
        }catch(RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", rtex.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") long userId){
        try{
            userService.deleteUserById(userId);
            return ResponseEntity.ok().body(Map.of("message", "The user has been deleted successfully"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", rtex.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        try{
            userService.updateUser(user);
            return ResponseEntity.ok().body(Map.of("message", "The user has been updated successfully"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }
}

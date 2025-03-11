package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.UserDto;
import com.alejandro.OpenEarth.dto.UserUpdateDto;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.AuthService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

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
            List<User> users = userService.getUsers();
            if(users.isEmpty())
                return ResponseEntity.noContent().build();

            List<UserDto> dtos = users.stream().map(UserDto::new).toList();

            return ResponseEntity.ok().body(Map.of("users", dtos));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", rtex.getMessage()));
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam("id") long userId){
        try{
            User user = userService.getUserById(userId);
            return ResponseEntity.ok().body(Map.of("user", new UserDto(user)));
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
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @Valid @RequestBody UserUpdateDto userDto, BindingResult result, @RequestParam("id") Long id){
        if(result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

        try{
            if (!jwtService.isThatMe(token, id))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "You are not allowed to update someone else"));

            authService.updateUser(userDto, id);
            return ResponseEntity.ok().body(Map.of("message", "The user has been updated successfully"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }
}

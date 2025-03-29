package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.UserCreationDto;
import com.alejandro.OpenEarth.dto.UserUpdateDto;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.EmailService;
import com.alejandro.OpenEarth.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("authService")
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("pictureService")
    private PictureService pictureService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;

    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userService.saveUser(user);

        return token;
    }

    private void validateUserRegistration(UserCreationDto userDto, Map<String, String> errors) {
        try{
            if (userService.getUserByEmail(userDto.getEmail()) != null)
                errors.put("email", "There is already an account with this email");
            if (userService.getUserByUsername(userDto.getUsername()) != null)
                errors.put("username", "There is already an account with this username");
            if (!userDto.getPassword().equals(userDto.getPasswordConfirmation()))
                errors.put("password", "Passwords do not match");
        }catch (Exception e){
         // We do not do anything since it means the user can be created
        }
    }

    public List<String> register(UserCreationDto userDto){
        Map<String, String> errors = new HashMap<>();
        validateUserRegistration(userDto, errors);

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }

        User user = new User();
        user.setRealUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userService.passwordEncoder().encode(userDto.getPassword()));
        user.setPicture(null);
        user.setCreationDate(LocalDate.now());
        user.setEnabled(true);
        user.setRole(userDto.getRole());

        String token = jwtService.generateToken(user);
        user.setToken(token);
        userService.saveUser(user);

        emailService.wellcome_email(user.getEmail());

        return List.of(token);
    }

    private void updatePassword(User user, String password, String confirmation, Map<String, String> errors) {
        if (password != null && confirmation != null && !confirmation.equals(password)) {
            errors.put("password", "Passwords do not match");
        } else if (password != null) {
            user.setPassword(userService.passwordEncoder().encode(password));
        }
    }

    private void updateUsername(User user, String username, Map<String, String> errors) {
        if (username != null && !username.equalsIgnoreCase(user.getRealUsername())) {
            try{
                if (userService.getUserByUsername(username) != null)
                    errors.put("username", "This username is already in use");
            }catch (UsernameNotFoundException e){
                user.setRealUsername(username);
            }
        }
    }

    private void updatePicture(User user, MultipartFile picture) {
        if (picture != null) {
            if (user.getPicture() != null) {
                pictureService.delete(user.getPicture());
            }
            pictureService.updateUserPicture(picture, user);
        }
    }

    public User updateUser(UserUpdateDto userDto, Long id, MultipartFile picture) {
        User user = userService.getUserById(id);
        Map<String, String> errors = new HashMap<>();

        updatePassword(user, userDto.getPassword(), userDto.getPasswordConfirmation(), errors);
        updateUsername(user, userDto.getUsername(), errors);

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }

        updatePicture(user, picture);
        return userService.saveUser(user);
    }

}

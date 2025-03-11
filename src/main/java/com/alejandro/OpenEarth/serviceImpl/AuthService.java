package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.UserCreationDto;
import com.alejandro.OpenEarth.dto.UserUpdateDto;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.PictureService;
import com.alejandro.OpenEarth.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public List<String> register(UserCreationDto userDto) throws RuntimeException{
        Map<String, String> errors = new HashMap<>();

        if (userService.getUserByEmail(userDto.getEmail()) != null)
            errors.put("email", "There is already an account with this email");
        if (userService.getUserByUsername(userDto.getUsername()) != null)
            errors.put("username", "There is already an account with this username");
        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation()))
            errors.put("password", "Passwords do not match");

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userService.passwordEncoder().encode(userDto.getPassword()));

        String token = jwtService.generateToken(user);
        user.setToken(token);
        userService.saveUser(user);

        pictureService.updateUserPicture(userDto.getPicture(), user);
        userService.saveUser(user);

        return List.of(token);
    }

    public User updateUser(UserUpdateDto userDto, Long id){
        Map<String, String> errors = new HashMap<>();
        User user = userService.getUserById(id);

        updatePassword(user, userDto.getPassword(), userDto.getPasswordConfirmation(), errors);
        updateUsername(user, userDto.getUsername(), errors);

        // Update picture
        MultipartFile picture = userDto.getPicture();
        if (picture != null) {
            Picture oldPicture = user.getPicture();
            if (oldPicture != null) {
                pictureService.delete(oldPicture); // Deletes old picture
            }
            pictureService.updateUserPicture(picture, user); // Sets new picture
        }

        return userService.saveUser(user);
    }

    private void updatePassword(User user, String password, String confirmation, Map<String, String> errors) {
        if (password != null && confirmation != null) {
            if (!confirmation.equals(password)) {
                errors.put("password", "Passwords do not match");
            } else {
                user.setPassword(userService.passwordEncoder().encode(password));
            }
        }
    }

    private void updateUsername(User user, String username, Map<String, String> errors) {
        if (username != null && !username.equalsIgnoreCase(user.getUsername())) {
            if (userService.getUserByUsername(username) != null) {
                errors.put("username", "This username is already in use");
            } else {
                user.setUsername(username);
            }
        }
    }

}

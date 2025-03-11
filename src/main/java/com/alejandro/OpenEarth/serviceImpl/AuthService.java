package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.UserCreationDto;
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

import java.util.List;

@Service("authService")
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("fileService")
    private StorageService storageService;

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
        userService.updateUser(user);

        return token;
    }

    public List<String> register(UserCreationDto userDto) throws RuntimeException{
        if (userService.getUserByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("There is already an account with this email");
        if (userService.getUserByUsername(userDto.getUsername()) != null)
            throw new RuntimeException("There is already an account with this username");
        if(!userDto.getPassword().equals(userDto.getPasswordConfirmation()))
            throw new RuntimeException("Passwords do not match");

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userService.passwordEncoder().encode(userDto.getPassword()));

        String token = jwtService.generateToken(user);
        user.setToken(token);
        userService.saveUser(user);

        // Picture related code
        String filename = storageService.store(userDto.getPicture(), user.getId());
        Picture picture = new Picture();
        picture.setUrl("/api/picture/" + filename);
        picture.setUser(user);
        picture.setHouse(null);
        pictureService.save(picture);
        user.setPicture(picture);
        userService.updateUser(user);

        return List.of(token);
    }
}

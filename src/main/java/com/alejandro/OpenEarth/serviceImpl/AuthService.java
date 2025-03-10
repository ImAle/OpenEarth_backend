package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("authService")
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

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

    // Checks data validity with user entity rules
    private List<String> checkForErrors(User user){
        List<String> errors = new ArrayList<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        for(ConstraintViolation<User> violation : violations){
            errors.add(violation.getMessage());
        }

        return errors;
    }
    public List<String> register(User user) throws RuntimeException{
        if (userService.getUserByEmail(user.getEmail()) != null)
            throw new RuntimeException("There is already an account with this email");

        List<String> errors = checkForErrors(user);

        if(!errors.isEmpty())
            return errors;

        String token = jwtService.generateToken(user);
        user.setToken(token);
        userService.saveUser(user);

        return List.of(token);
    }
}

package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.dto.ReviewDto;
import com.alejandro.OpenEarth.dto.UserDto;
import com.alejandro.OpenEarth.entity.UserRole;
import com.alejandro.OpenEarth.repository.UserRepository;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService implements UserDetailsService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("pictureService")
    private PictureService pictureService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findByRoleNot(UserRole.ADMIN);
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            throw new RuntimeException("User not found");

        return user.get();
    }

    public User getUserFullDetailById(Long id){
        Optional<User> user = userRepository.findFullDetailUser(id);
        if(user.isEmpty())
            throw new RuntimeException("User not found");

        return user.get();
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }

    public User getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }

    public void updateUserPhoto(User user, MultipartFile picture){
        pictureService.updateUserPicture(picture, user);
        saveUser(user);
    }

    public void deleteUserById(Long id){
        User user = this.getUserById(id);
        pictureService.delete(user.getPicture());
        userRepository.delete(user);
    }

    public void activateUserById(Long id) {
        User user = this.getUserById(id);
        if (user.isEnabled())
            throw new RuntimeException("User is already activated");

        user.setEnabled(true);
        this.saveUser(user);
    }

    public void deactivateUserById(Long id){
        User user = this.getUserById(id);
        if (!user.isEnabled())
            throw new RuntimeException("User is already deactivated");

        user.setEnabled(false);
        this.saveUser(user);
    }

}

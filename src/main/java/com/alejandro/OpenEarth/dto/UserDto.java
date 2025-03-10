package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private boolean enabled;
    private String picture;
    private Set<House> houses = new HashSet<>();
    private Set<Rent> rents = new HashSet<>();
    private Set<Review> reviews = new HashSet<>();

    public UserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.isEnabled());
        userDto.setRole(user.getRole());
        userDto.setEnabled(user.isEnabled());
        userDto.setHouses(user.getHouses());
        userDto.setRents(user.getRents());
        userDto.setReviews(user.getReviews());
        userDto.setPicture(user.getPicture().getUrl());
    }
}

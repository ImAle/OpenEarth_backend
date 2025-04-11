package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
        this.setId(user.getId());
        this.setUsername(user.getRealUsername());
        this.setFirstName(user.getFirstname());
        this.setLastName(user.getLastname());
        this.setEmail(user.getEmail());
        this.setEnabled(user.isEnabled());
        this.setRole(user.getRole());
        this.setEnabled(user.isEnabled());
        this.setHouses(user.getHouses());
        this.setRents(user.getRents());
        this.setReviews(user.getReviews());
        this.setPicture(user.getPicture() != null ? user.getPicture().getUrl() : null);
    }
}

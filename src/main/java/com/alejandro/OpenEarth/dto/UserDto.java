package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<HousePreviewDto> houses = new ArrayList<>();
    private List<RentDto> rents = new ArrayList<>();
    private List<ReviewDto> reviews = new ArrayList<>();
    private LocalDate creationDate;

    public UserDto(User user) {
        this.setId(user.getId());
        this.setUsername(user.getRealUsername());
        this.setFirstName(user.getFirstname());
        this.setLastName(user.getLastname());
        this.setEmail(user.getEmail());
        this.setEnabled(user.isEnabled());
        this.setRole(user.getRole());
        this.setEnabled(user.isEnabled());
        this.creationDate = user.getCreationDate();

        this.setHouses(user.getHouses().stream().map(h -> new HousePreviewDto(h, "EUR")).toList());
        this.setRents(user.getRents().stream().map(RentDto::new).toList());
        this.setReviews(user.getReviews().stream().map(ReviewDto::new).toList());
        this.setPicture(user.getPicture() != null ? user.getPicture().getUrl() : null);
    }
}

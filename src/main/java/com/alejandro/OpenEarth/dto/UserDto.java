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

}

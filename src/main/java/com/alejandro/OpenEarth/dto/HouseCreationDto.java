package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class HouseCreationDto {

    private String title;
    private String description;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double price;
    private String location;
    private String category;
    private Set<String> pictures;
    private Long idOwner;

    public House fromDtoToEntity(HouseCreationDto houseDto) {
        House house = new House();
        house.setTitle(houseDto.getTitle());
        house.setDescription(houseDto.getDescription());
        house.setGuests(houseDto.getGuests());
        house.setBedrooms(houseDto.getBedrooms());
        house.setBeds(houseDto.getBeds());
        house.setLocation(houseDto.getLocation());
        house.setCategory(HouseCategory.valueOf(houseDto.getCategory()));
        house.setPrice(houseDto.getPrice());
        house.setCreationDate(LocalDate.now());
        house.setLastUpdateDate(LocalDate.now());
        house.setRents(null);
        house.setReviews(null);
        // lacks to set pictures

        UserService userService = new UserService();
        User user = userService.getUserById(houseDto.getIdOwner());
        house.setOwner(user);

        return house;
    }

}

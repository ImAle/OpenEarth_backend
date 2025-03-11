package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.serviceImpl.HouseServiceImpl;
import com.alejandro.OpenEarth.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class HouseUpdateDto {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "title should be between 2 and 100 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 100, max = 1200, message = "Description must be between 100 and 1200 characters")
    private String description;
    @NotBlank(message = "Number of guests is required")
    @Size(min = 1, message = "The number of guest must be higher or equals to 1")
    private int guests;
    @NotBlank(message = "Number of bedrooms is required")
    @Size(min = 0, message = "The number of bedrooms must be higher or equals to 0")
    private int bedrooms;
    @NotBlank(message = "Number of beds is required")
    @Size(min = 1, message = "The number of bedrooms must be higher or equals to 1")
    private int beds;
    @NotBlank(message = "Number of bathrooms is required")
    @Size(min = 1, message = "The number of bathrooms must be higher or equals to 1")
    private int bathrooms;
    @NotBlank(message = "Price is required")
    @Size(min = 1, message = "Price must be higher than 1")
    private double price;
    @ValidEnum(enumClass = HouseCategory.class, message = "Invalid house category")
    private String category;
    @ValidEnum(enumClass = HouseStatus.class, message = "Invalid house category")
    private String status;
    @NotBlank(message = "Pictures are required")
    @Size(min = 1, message = "Provide at least 1 picture")
    private Set<Picture> pictures;

    public House fromDtoToEntity(HouseUpdateDto houseUpdateDto, Long id) {
        HouseServiceImpl houseService = new HouseServiceImpl();
        House house = houseService.getHouseById(id);
        house.setTitle(houseUpdateDto.getTitle());
        house.setDescription(houseUpdateDto.getDescription());
        house.setGuests(houseUpdateDto.getGuests());
        house.setBedrooms(houseUpdateDto.getBedrooms());
        house.setBeds(houseUpdateDto.getBeds());
        house.setBathrooms(houseUpdateDto.getBathrooms());
        house.setPrice(houseUpdateDto.getPrice());
        house.setCategory(HouseCategory.valueOf(houseUpdateDto.getCategory()));
        house.setStatus(HouseStatus.valueOf(houseUpdateDto.getStatus()));
        house.setPictures(houseUpdateDto.getPictures());

        return house;
    }

    public HouseUpdateDto fromEntityToDto(House house) {
        HouseUpdateDto houseUpdateDto = new HouseUpdateDto();
        houseUpdateDto.setTitle(house.getTitle());
        houseUpdateDto.setDescription(house.getDescription());
        houseUpdateDto.setGuests(house.getGuests());
        houseUpdateDto.setBedrooms(house.getBedrooms());
        houseUpdateDto.setBeds(house.getBeds());
        houseUpdateDto.setBathrooms(house.getBathrooms());
        houseUpdateDto.setPrice(house.getPrice());
        houseUpdateDto.setCategory(house.getCategory().toString());
        houseUpdateDto.setStatus(house.getStatus().toString());
        houseUpdateDto.setPictures(house.getPictures());

        return houseUpdateDto;
    }
}

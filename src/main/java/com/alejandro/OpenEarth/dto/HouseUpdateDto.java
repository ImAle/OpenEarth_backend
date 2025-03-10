package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.serviceImpl.HouseServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class HouseUpdateDto {
    private Long id;
    private String title;
    private String description;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double price;
    private String category;
    private String status;
    private Set<Picture> pictures;

    public House fromDtoToEntity(HouseUpdateDto houseUpdateDto) {
        HouseServiceImpl houseService = new HouseServiceImpl();
        House house = houseService.getHouseById(houseUpdateDto.getId());
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
        houseUpdateDto.setId(house.getId());
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

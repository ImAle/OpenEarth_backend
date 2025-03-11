package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class HouseUpdateFormDto {

    private String title;
    private String description;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double price;
    private String category;
    private String status;
    private Set<PictureDto> pictures;

    public HouseUpdateFormDto(House house) {
        this.setTitle(house.getTitle());
        this.setDescription(house.getDescription());
        this.setGuests(house.getGuests());
        this.setBedrooms(house.getBedrooms());
        this.setBeds(house.getBeds());
        this.setBathrooms(house.getBathrooms());
        this.setPrice(house.getPrice());
        this.setCategory(house.getCategory().toString());
        this.setStatus(house.getStatus().toString());

        Set<PictureDto> pictures = new HashSet<>();
        for(Picture picture : house.getPictures()) {
            pictures.add(new PictureDto(picture.getId(), picture.getUrl()));
        }
        this.setPictures(pictures);
    }

}

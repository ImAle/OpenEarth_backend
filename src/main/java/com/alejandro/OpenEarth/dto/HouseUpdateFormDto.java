package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String currency;
    private String category;
    private String status;
    private Set<PictureDto> pictures;

}

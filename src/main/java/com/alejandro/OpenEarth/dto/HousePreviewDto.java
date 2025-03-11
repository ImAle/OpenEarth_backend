package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Picture;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class HousePreviewDto {

    private Long id;
    private String title;
    private String country;
    private String location;
    private String coordinates;
    private double price;
    private Set<String> pictures = new HashSet<>();

    public HousePreviewDto(House house){
        HousePreviewDto dto = new HousePreviewDto();
        dto.setId(house.getId());
        dto.setTitle(house.getTitle());
        dto.setCountry(house.getCountry().getFormattedName());
        dto.setLocation(house.getLocation());
        dto.setCoordinates(house.getCoordinates());
        dto.setPrice(house.getPrice());
        dto.setPictures(house.getPictures()
                .stream().map(Picture::getUrl)
                .collect(Collectors.toSet()));
    }
}

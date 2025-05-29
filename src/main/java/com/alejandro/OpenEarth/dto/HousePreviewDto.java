package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.service.CurrencyService;
import com.alejandro.OpenEarth.serviceImpl.CurrencyServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class HousePreviewDto {

    private Long id;
    private String title;
    private String location;
    private double latitude;
    private double longitude;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double price;
    private String currency;
    private Set<String> pictures = new HashSet<>();

}

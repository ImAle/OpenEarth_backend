package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.service.CurrencyService;
import com.alejandro.OpenEarth.serviceImpl.CurrencyServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class HouseDetailsDto {

    private Long id;
    private String title;
    private String description;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double price;
    private String currency;
    private String location;
    private double latitude;
    private double longitude;
    private HouseCategory category;
    private HouseStatus status;
    private LocalDate creationDate;
    private Set<PictureDto> pictures = new HashSet<>();
    private UserInfoDto owner;
    private Set<ReviewDto> reviews = new HashSet<>();


}

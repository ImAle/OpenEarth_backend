package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
    private String location;
    private String coordinates;
    private HouseCategory category;
    private HouseStatus status;
    private LocalDate creationDate;
    private Set<String> pictures;
    private UserDto owner;
    private Set<Review> reviews = new HashSet<>();

    public HouseDetailsDto(House house) {
        this.id = house.getId();
        this.title = house.getTitle();
        this.description = house.getDescription();
        this.guests = house.getGuests();
        this.bedrooms = house.getBedrooms();
        this.beds = house.getBeds();
        this.bathrooms = house.getBathrooms();
        this.price = house.getPrice();
        this.location = house.getLocation();
        this.coordinates = house.getCoordinates();
        this.category = house.getCategory();
        this.status = house.getStatus();
        this.creationDate = house.getCreationDate();
        this.setPictures(house.getPictures()
                .stream().map(Picture::getUrl)
                .collect(Collectors.toSet()));
        this.setOwner(new UserDto(house.getOwner()));
        this.setReviews(house.getReviews());
    }

}

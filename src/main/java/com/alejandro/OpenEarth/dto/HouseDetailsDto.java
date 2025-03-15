package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.service.CurrencyService;
import com.alejandro.OpenEarth.serviceImpl.CurrencyServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

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
    private String country;
    private String location;
    private String coordinates;
    private HouseCategory category;
    private HouseStatus status;
    private LocalDate creationDate;
    private Set<PictureDto> pictures = new HashSet<>();
    private UserDto owner;
    private List<ReviewDto> reviews = new ArrayList<>();

    public HouseDetailsDto(House house) {
        this.id = house.getId();
        this.title = house.getTitle();
        this.description = house.getDescription();
        this.guests = house.getGuests();
        this.bedrooms = house.getBedrooms();
        this.beds = house.getBeds();
        this.bathrooms = house.getBathrooms();
        this.country = house.getCountry().getFormattedName();
        this.location = house.getLocation();
        this.coordinates = house.getCoordinates();
        this.category = house.getCategory();
        this.status = house.getStatus();
        this.creationDate = house.getCreationDate();
        this.setOwner(new UserDto(house.getOwner()));

        double price = house.getPrice();

        if(!Objects.equals(this.currency, "EUR")){
            CurrencyService currencyService = new CurrencyServiceImpl();
            price = currencyService.getPriceInSelectedCurrency(this.currency, house.getPrice());
        }

        this.price = price;


        List<ReviewDto> reviews = house.getReviews().stream().map(ReviewDto::new).toList();
        this.setReviews(reviews);

        Set<PictureDto> pictures = new HashSet<>();
        for(Picture picture : house.getPictures()){
            pictures.add(new PictureDto(picture.getId(), picture.getUrl()));
        }

        this.setPictures(pictures);

    }

}

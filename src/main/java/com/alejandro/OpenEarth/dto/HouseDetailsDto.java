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
    private List<ReviewDto> reviews = new ArrayList<>();

    public HouseDetailsDto(House house, String currency) {
        this.id = house.getId();
        this.title = house.getTitle();
        this.description = house.getDescription();
        this.guests = house.getGuests();
        this.bedrooms = house.getBedrooms();
        this.beds = house.getBeds();
        this.bathrooms = house.getBathrooms();
        this.location = house.getLocation();
        this.latitude = house.getLatitude();
        this.longitude = house.getLongitude();
        this.category = house.getCategory();
        this.status = house.getStatus();
        this.currency = currency;
        this.creationDate = house.getCreationDate();
        this.setOwner(new UserInfoDto(house.getOwner()));
        double price = house.getPrice();

        if(!Objects.equals(this.currency, "EUR")){
            CurrencyService currencyService = new CurrencyServiceImpl();
            price = currencyService.getPriceInSelectedCurrency(this.currency, house.getPrice());
        }

        this.price = Math.round(price);

        List<ReviewDto> reviews = house.getReviews().stream().map(ReviewDto::new).toList();
        this.setReviews(reviews);
        this.pictures = house.getPictures()
                .stream()
                .map(p -> new PictureDto(p.getId(), p.getUrl()))
                .collect(Collectors.toSet());


        this.setPictures(pictures);
    }

}

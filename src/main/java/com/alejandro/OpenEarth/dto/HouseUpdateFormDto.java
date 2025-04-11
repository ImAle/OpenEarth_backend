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

    public HouseUpdateFormDto(House house, String currency) {
        this.setTitle(house.getTitle());
        this.setDescription(house.getDescription());
        this.setGuests(house.getGuests());
        this.setBedrooms(house.getBedrooms());
        this.setBeds(house.getBeds());
        this.setBathrooms(house.getBathrooms());
        this.setCategory(house.getCategory().toString());
        this.setStatus(house.getStatus().toString());
        this.currency = currency;

        Set<PictureDto> pictures = new HashSet<>();
        for(Picture picture : house.getPictures()) {
            pictures.add(new PictureDto(picture.getId(), picture.getUrl()));
        }
        this.setPictures(pictures);

        double price = house.getPrice();

        if(!Objects.equals(this.currency, "EUR")){
            CurrencyService currencyService = new CurrencyServiceImpl();
            price = currencyService.getPriceInSelectedCurrency(this.currency, house.getPrice());
        }

        this.price = price;
    }

}

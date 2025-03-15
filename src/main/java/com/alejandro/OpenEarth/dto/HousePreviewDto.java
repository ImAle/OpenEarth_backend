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
    private String country;
    private String location;
    private String coordinates;
    private double price;
    private String currency;
    private Set<String> pictures = new HashSet<>();

    public HousePreviewDto(House house){
        this.setId(house.getId());
        this.setTitle(house.getTitle());
        this.setCountry(house.getCountry().getFormattedName());
        this.setLocation(house.getLocation());
        this.setCoordinates(house.getCoordinates());
        this.setPictures(house.getPictures()
                .stream().map(Picture::getUrl)
                .collect(Collectors.toSet()));

        double price = house.getPrice();

        if(!Objects.equals(this.currency, "EUR")){
            CurrencyService currencyService = new CurrencyServiceImpl();
            price = currencyService.getPriceInSelectedCurrency(this.currency, house.getPrice());
        }

        this.price = price;
    }
}

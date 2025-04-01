package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Country;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.validation.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseCreationDto {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "title should be between 2 and 100 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 100, max = 1200, message = "Description must be between 100 and 1200 characters")
    private String description;
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "The number of guest must be higher or equals to 1")
    private int guests;
    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 0, message = "The number of bedrooms must be higher or equals to 0")
    private int bedrooms;
    @NotNull(message = "Number of beds is required")
    @Min(value = 1, message = "The number of bedrooms must be higher or equals to 1")
    private int beds;
    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 1, message = "The number of bathrooms must be higher or equals to 1")
    private int bathrooms;
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be higher than 1")
    private double price;
    @NotBlank(message = "Choose currency")
    private String currency;
    @ValidEnum(enumClass = Country.class, message = "Invalid country")
    private Country country;
    @NotBlank(message = "Location is required")
    @Size(min = 6, message = "Provide a valid location")
    private String location;
    @ValidEnum(enumClass = HouseCategory.class, message = "Invalid house category")
    private String category;
}

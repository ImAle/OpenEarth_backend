package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseUpdateDto {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "title should be between 2 and 100 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 100, max = 65530, message = "Description must be between 100 and 65530 characters")
    private String description;
    @NotBlank(message = "Number of guests is required")
    @Size(min = 1, message = "The number of guest must be higher or equals to 1")
    private int guests;
    @NotBlank(message = "Number of bedrooms is required")
    @Size(min = 0, message = "The number of bedrooms must be higher or equals to 0")
    private int bedrooms;
    @NotBlank(message = "Number of beds is required")
    @Size(min = 1, message = "The number of bedrooms must be higher or equals to 1")
    private int beds;
    @NotBlank(message = "Number of bathrooms is required")
    @Size(min = 1, message = "The number of bathrooms must be higher or equals to 1")
    private int bathrooms;
    @NotBlank(message = "Price is required")
    @Size(min = 1, message = "Price must be higher than 1")
    private double price;
    @NotBlank(message = "Choose currency")
    private String currency;
    @ValidEnum(enumClass = HouseCategory.class, message = "Invalid house category")
    private String category;
    @ValidEnum(enumClass = HouseStatus.class, message = "Invalid house category")
    private String status;
}

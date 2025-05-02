package com.alejandro.OpenEarth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewCreationDto {

    @NotBlank(message = "The review requires a comment")
    @Size(min = 10, max = 500, message = "The house review must be between 10 and 500 characters")
    private String comment;
    @NotNull(message = "House is required")
    private Long houseId;

}

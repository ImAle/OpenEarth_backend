package com.alejandro.OpenEarth.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RentCreationDto {

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate startTime;
    @NotNull(message = "End time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate endTime;
    @NotBlank(message = "House is required")
    private Long houseId;

}

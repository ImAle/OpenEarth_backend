package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RentCreationDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long userId;
    private Long houseId;

}

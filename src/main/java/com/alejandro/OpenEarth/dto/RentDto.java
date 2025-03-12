package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Rent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RentDto {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long userId;
    private Long houseId;

    public RentDto(Rent rent) {
        this.id = rent.getId();
        this.startTime = rent.getStartDate();
        this.endTime = rent.getEndDate();
        this.userId = rent.getUser().getId();
        this.houseId = rent.getHouse().getId();
    }
}

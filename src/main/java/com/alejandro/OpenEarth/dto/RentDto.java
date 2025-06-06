package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Rent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RentDto {

    private Long id;
    private double price;
    private LocalDate startTime;
    private LocalDate endTime;
    private boolean cancelled;
    private Long userId;
    private Long houseId;

    public RentDto(Rent rent) {
        this.id = rent.getId();
        this.startTime = rent.getStartDate();
        this.endTime = rent.getEndDate();
        this.userId = rent.getUser().getId();
        this.houseId = rent.getHouse().getId();
        this.price = rent.getPrice();
        this.cancelled = rent.isCancelled();
    }
}

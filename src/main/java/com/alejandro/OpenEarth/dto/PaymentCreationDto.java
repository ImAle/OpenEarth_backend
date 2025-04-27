package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentCreationDto {
    private String currency;
    private String description;
    private Double amount;
}

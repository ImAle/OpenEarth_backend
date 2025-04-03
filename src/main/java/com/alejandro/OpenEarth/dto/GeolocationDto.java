package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeolocationDto {
    private String latitude;
    private String longitude;
    private String location;
}

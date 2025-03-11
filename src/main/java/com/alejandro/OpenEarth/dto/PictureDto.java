package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PictureDto {
    private Long id;
    private String url;

    public PictureDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }
}

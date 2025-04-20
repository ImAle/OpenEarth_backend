package com.alejandro.OpenEarth.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    @Size(min=8, message = "Password must be higher than 8 characters")
    private String password;
    private String passwordConfirmation;

}

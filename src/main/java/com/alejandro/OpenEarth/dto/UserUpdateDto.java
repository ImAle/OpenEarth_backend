package com.alejandro.OpenEarth.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    @Size(min= 5, max=15, message = "Username must be higher than 5 and less than 15")
    private String username;
    @Size(min=8, message = "Password must be higher than 8 characters")
    private String password;
    private String passwordConfirmation;

}

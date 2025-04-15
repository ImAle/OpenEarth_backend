package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.UserRole;
import com.alejandro.OpenEarth.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreationDto {

    @NotBlank(message = "Username is required")
    @Size(min= 5, max=15, message = "Username must be higher than 5 and less than 15")
    private String username;
    @NotBlank(message = "Firstname is required")
    private String firstname;
    @NotBlank(message = "Lastname is required")
    private String lastname;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be higher than 8 characters")
    private String password;
    @NotBlank(message = "Password Confirmation is required")
    private String passwordConfirmation;
    @ValidEnum(enumClass = UserRole.class, message = "Invalid role")
    private UserRole role;
}

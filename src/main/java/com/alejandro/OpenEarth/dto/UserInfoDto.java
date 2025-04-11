package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String picture;

    UserInfoDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
        this.picture = user.getPicture() != null ? user.getPicture().getUrl() : null;
    }
}

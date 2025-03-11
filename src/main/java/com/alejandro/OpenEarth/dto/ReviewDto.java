package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Review;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.HouseServiceImpl;
import com.alejandro.OpenEarth.serviceImpl.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewDto {

    @NotBlank
    @Size(min = 10, max = 500)
    private String comment;
    @NotBlank
    private Long houseId;
    @NotBlank
    private Long userId;

    public Review fromDtoToEntity(ReviewDto dto) {
        HouseServiceImpl houseService = new HouseServiceImpl();
        UserService userService = new UserService();
        Review review = new Review();
        review.setComment(dto.comment);

        User user = userService.getUserById(dto.userId);
        House house = houseService.getHouseById(dto.houseId);

        review.setHouse(house);
        review.setUser(user);
        return review;
    }
}

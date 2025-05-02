package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String comment;
    private Long houseId;
    private Long userId;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.comment = review.getComment();
        this.houseId = review.getHouse().getId();
        this.userId = review.getUser().getId();
    }
}

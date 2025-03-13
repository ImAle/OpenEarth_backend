package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Review;
import com.alejandro.OpenEarth.service.ReviewService;
import com.alejandro.OpenEarth.serviceImpl.ReviewServiceImpl;
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
        ReviewService reviewService = new ReviewServiceImpl();
        ReviewDto dto = reviewService.getReviewToShow(review);
        this.id = review.getId();
        this.comment = review.getComment();
        this.houseId = review.getHouse().getId();
        this.userId = review.getUser().getId();
    }
}

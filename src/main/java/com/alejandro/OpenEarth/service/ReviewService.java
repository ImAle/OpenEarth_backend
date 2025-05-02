package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.ReviewCreationDto;
import com.alejandro.OpenEarth.dto.ReviewDto;
import com.alejandro.OpenEarth.entity.Review;

import java.util.List;
import java.util.Set;

public interface ReviewService {
    Review saveReview(Review review);
    Review createReview(String token, ReviewCreationDto dto);
    ReviewDto getReviewToShow(Review review);
    Set<Review> getReviewFromHouseId(Long houseId);
}

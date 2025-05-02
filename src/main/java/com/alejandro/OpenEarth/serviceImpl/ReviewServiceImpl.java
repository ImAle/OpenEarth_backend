package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.ReviewCreationDto;
import com.alejandro.OpenEarth.dto.ReviewDto;
import com.alejandro.OpenEarth.entity.Review;
import com.alejandro.OpenEarth.repository.ReviewRepository;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    @Qualifier("reviewRepository")
    private ReviewRepository reviewRepository;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review createReview(String token, ReviewCreationDto dto) {
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setUser(jwtService.getUser(token));
        review.setHouse(houseService.getHouseById(dto.getHouseId()));

        return saveReview(review);
    }

    @Override
    public ReviewDto getReviewToShow(Review review){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        userService.getUserById(review.getUser().getId());
        houseService.getHouseById(review.getHouse().getId());

        return reviewDto;
    }

    @Override
    public Set<Review> getReviewFromHouseId(Long houseId) {
        houseService.getHouseById(houseId); // throws error if house does not exist
        return reviewRepository.findByHouse_Id(houseId);
    }
}

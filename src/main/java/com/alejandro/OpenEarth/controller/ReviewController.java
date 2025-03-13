package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.ReviewCreationDto;
import com.alejandro.OpenEarth.entity.Review;
import com.alejandro.OpenEarth.service.ReviewService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    @Qualifier("reviewService")
    private ReviewService reviewService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @Valid @RequestBody ReviewCreationDto reviewDto, BindingResult result) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            if(!Objects.equals(jwtService.getUser(token).getId(), jwtService.getUser(token).getId()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You can not write a review on behalf of someone else");

            Review review = reviewService.createReview(token, reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("review", review));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rtex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

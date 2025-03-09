package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("reviewRepository")
public interface ReviewRepository extends JpaRepository<Review, Long> {

}

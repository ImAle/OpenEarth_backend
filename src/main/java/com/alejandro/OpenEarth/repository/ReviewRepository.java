package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("reviewRepository")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByHouse_Id(Long houseId);
}

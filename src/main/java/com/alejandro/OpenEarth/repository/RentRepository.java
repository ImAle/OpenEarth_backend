package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("rentRepository")
public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT r FROM Rent r WHERE r.user.id = :userId AND :now BETWEEN r.startDate AND r.endDate")
    List<Rent> findActiveRentsByUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);

}

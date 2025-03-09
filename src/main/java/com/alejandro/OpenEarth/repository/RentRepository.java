package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("rentRepository")
public interface RentRepository extends JpaRepository<Rent, Long> {

}

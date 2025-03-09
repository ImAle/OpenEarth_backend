package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("houseRepository")
public interface HouseRepository extends JpaRepository<House, Long> {

}

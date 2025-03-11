package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("houseRepository")
public interface HouseRepository extends JpaRepository<House, Long> {
    Set<House> findByStatus(HouseStatus status);

    @Query("SELECT h.id FROM House h WHERE h.owner.id = :ownerId")
    List<Long> findHousesIdByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT h FROM House h WHERE h.status = :status AND h.owner.enabled = true")
    Set<House> findByStatusAndEnabledOwners(@Param("status") HouseStatus status);
}

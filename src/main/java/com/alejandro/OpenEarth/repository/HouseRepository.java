package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
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
    List<House> findByStatusAndEnabledOwners(@Param("status") HouseStatus status);


    @Query("""
    SELECT h FROM House h
    WHERE h.owner.enabled = true
      AND h.status = 'AVAILABLE'
      AND (:minPrice IS NULL OR h.price >= :minPrice)
      AND (:maxPrice IS NULL OR h.price <= :maxPrice)
      AND (:beds IS NULL OR h.beds = :beds)
      AND (:guests IS NULL OR h.guests = :guests)
      AND (:category IS NULL OR h.category = :category)
    """)
    List<House> findHousesByFilters(@Param("minPrice") Double minPrice,
                                         @Param("maxPrice") Double maxPrice,
                                         @Param("beds") Integer beds,
                                         @Param("guests") Integer guests,
                                         @Param("category") HouseCategory category);

    @Query("SELECT h FROM House h WHERE h.latitude BETWEEN :minLat AND :maxLat AND h.longitude BETWEEN :minLng AND :maxLng")
    List<House> findInArea(@Param("minLat") double minLat,
                                     @Param("maxLat") double maxLat,
                                     @Param("minLng") double minLng,
                                     @Param("maxLng") double maxLng);

}

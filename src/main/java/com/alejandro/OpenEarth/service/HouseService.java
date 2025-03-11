package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.HouseUpdateDto;
import com.alejandro.OpenEarth.entity.Country;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;

import java.util.List;
import java.util.Set;

public interface HouseService {
    House create(HouseCreationDto houseDto);
    House getHouseById(Long id);
    Set<House> getHouses();
    List<HousePreviewDto> getAllAvailableHouses();
    List<HousePreviewDto> getFilteredHouses(Country country, double minPrice, double maxPrice, Integer beds, Integer guests, HouseCategory category);
    HouseStatus[] getHouseStatuses();
    List<Long> getIdHousesByOwnerId(Long ownerId);
    boolean isMyHouse(String token, Long houseId);
    HouseCategory[] getHouseCategories();
    House updateHouse(HouseUpdateDto houseDto, Long id);
    void deleteHouseById(Long id);
}

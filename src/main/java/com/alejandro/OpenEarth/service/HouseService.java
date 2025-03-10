package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
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
    HouseStatus[] getHouseStatuses();
    List<Long> getIdHousesByOwnerId(Long ownerId);
    boolean isMyHouse(String token, Long houseId);
    HouseCategory[] getHouseCategories();
    House updateHouse(House house);
    void deleteHouseById(Long id);
}

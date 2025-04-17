package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.HouseUpdateDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface HouseService {
    House create(String token, HouseCreationDto houseDto, MultipartFile[] images);
    House getHouseById(Long id);
    Set<House> getHouses();
    List<House> getAllAvailableHouses(String currency);
    List<HousePreviewDto> getHousesNearTo(House house, double km);
    List<HousePreviewDto> getFilteredHouses(String location, Double minPrice, Double maxPrice, Integer beds, Integer guests, HouseCategory category, String currency);
    HouseStatus[] getHouseStatuses();
    List<Long> getIdHousesByOwnerId(Long ownerId);
    Set<House> getHousesofLoggedUser(String token);
    boolean isMyHouse(String token, Long houseId);
    HouseCategory[] getHouseCategories();
    House updateHouse(HouseUpdateDto houseDto, Long id, List<MultipartFile> newPictures);
    void deleteHouseById(Long id);
}

package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.HouseUpdateDto;
import com.alejandro.OpenEarth.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface HouseService {
    House save(House house);
    House create(String token, HouseCreationDto houseDto, MultipartFile[] images);
    House getHouseById(Long id);
    Set<House> getHouses();
    List<House> getAllAvailableHouses(String currency);
    List<HousePreviewDto> getHousesNearTo(House house, double km, String currency);
    List<HousePreviewDto> getFilteredHouses(String location, Double minPrice, Double maxPrice, Integer beds, Integer guests, HouseCategory category, String currency);
    HouseStatus[] getHouseStatuses();
    List<House> getHousesByOwnerId(Long ownerId);
    Set<House> getHousesofLoggedUser(String token);
    boolean isMyHouse(String token, Long houseId);
    HouseCategory[] getHouseCategories();
    House updateHouse(HouseUpdateDto houseDto, Long id, MultipartFile[] newPictures);
    void deleteHouseById(Long id);
    boolean isThatMyPictureHouse(Picture picture, User user);
}

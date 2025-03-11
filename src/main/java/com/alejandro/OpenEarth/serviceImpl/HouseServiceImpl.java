package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.repository.HouseRepository;
import com.alejandro.OpenEarth.repository.PictureRepository;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.PictureService;
import com.alejandro.OpenEarth.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("houseService")
public class HouseServiceImpl implements HouseService {

    @Autowired
    @Qualifier("houseRepository")
    private HouseRepository houseRepository;

    @Autowired
    @Qualifier("fileService")
    private StorageService storageService;

    @Autowired
    @Qualifier("pictureService")
    private PictureService pictureService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public House create(HouseCreationDto houseDto) {
        User owner = userService.getUserById(houseDto.getIdOwner());
        House house = new House();

        house.setTitle(houseDto.getTitle());
        house.setDescription(houseDto.getDescription());
        house.setGuests(houseDto.getGuests());
        house.setBedrooms(houseDto.getBedrooms());
        house.setBeds(houseDto.getBeds());
        house.setLocation(houseDto.getLocation());
        house.setCategory(HouseCategory.valueOf(houseDto.getCategory()));
        house.setPrice(houseDto.getPrice());
        house.setCreationDate(LocalDate.now());
        house.setLastUpdateDate(LocalDate.now());
        house.setRents(null);
        house.setReviews(null);
        house.setOwner(owner);

        houseRepository.save(house);

        Set<Picture> pictures = new HashSet<>();
        for(MultipartFile file : houseDto.getPictures()){
            // Save image
            String filename = storageService.store(file, house.getId());

            Picture picture = pictureService.createHousePicture(filename, house);

            // Save and Add to Set<Picture>
            pictureService.save(picture);
            pictures.add(picture);
        }

        house.setPictures(pictures);

        return houseRepository.save(house);

    }

    @Override
    public House getHouseById(Long id) {
        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty())
            throw new RuntimeException("House not found.");

        return house.get();
    }

    @Override
    public Set<House> getHouses() {
        return new HashSet<>(houseRepository.findAll());
    }

    @Override
    public List<HousePreviewDto> getAllAvailableHouses() {
        HousePreviewDto housePreviewDto = new HousePreviewDto();
        Set<House> houses = houseRepository.findByStatus(HouseStatus.AVAILABLE);
        List<HousePreviewDto> dtos = houses.stream().map(housePreviewDto::fromEntityToDto).toList();

        return dtos;
    }

    @Override
    public HouseStatus[] getHouseStatuses() {
        return HouseStatus.values();
    }

    @Override
    public HouseCategory[] getHouseCategories() {
        return HouseCategory.values();
    }

    @Override
    public List<Long> getIdHousesByOwnerId(Long ownerId) {
        return houseRepository.findHousesIdByOwnerId(ownerId);
    }

    @Override
    public boolean isMyHouse(String token, Long houseId) {
        User user = jwtService.getUser(token);
        List<Long> myHouses = houseRepository.findHousesIdByOwnerId(user.getId());

        return myHouses.contains(houseId);
    }

    @Override
    public House updateHouse(House house) {
        house.setLastUpdateDate(LocalDate.now());
        return houseRepository.save(house);
    }

    @Override
    public void deleteHouseById(Long id) {
        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty())
            throw new RuntimeException("House not found");

        for(Picture picture : house.get().getPictures())
            pictureService.delete(picture);

        houseRepository.deleteById(id);
    }
}

package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.HouseRepository;
import com.alejandro.OpenEarth.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public House create(House house) {
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

        houseRepository.deleteById(id);
    }
}

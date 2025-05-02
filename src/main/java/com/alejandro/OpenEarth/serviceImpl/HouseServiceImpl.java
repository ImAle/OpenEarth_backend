package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.HouseUpdateDto;
import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.repository.HouseRepository;
import com.alejandro.OpenEarth.service.*;
import com.alejandro.OpenEarth.upload.StorageService;
import com.alejandro.OpenEarth.utility.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

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
    @Qualifier("geolocationService")
    private GeolocationService geolocationService;

    @Autowired
    @Qualifier("geoPolygonService")
    private GeoPolygonService geoPolygonService;

    @Autowired
    @Qualifier("currencyService")
    private CurrencyService currencyService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public House save(House house){
        return houseRepository.save(house);
    }


    @Override
    public House create(String token, HouseCreationDto houseDto, MultipartFile[] images) {
        Long owner = jwtService.getUserId(token);
        User user = userService.getUserFullDetailById(owner);
        House house = new House();
        house.setTitle(houseDto.getTitle());
        house.setDescription(houseDto.getDescription());
        house.setGuests(houseDto.getGuests());
        house.setBedrooms(houseDto.getBedrooms());
        house.setBeds(houseDto.getBeds());
        house.setBathrooms(houseDto.getBathrooms());
        house.setLocation(houseDto.getLocation());
        house.setCreationDate(LocalDate.now());
        house.setLastUpdateDate(LocalDate.now());
        house.setStatus(HouseStatus.AVAILABLE);
        house.setLatitude(houseDto.getLatitude());
        house.setLongitude(houseDto.getLongitude());
        house.setCategory(HouseCategory.valueOf(houseDto.getCategory()));
        house.setRents(null);
        house.setReviews(null);
        house.setOwner(user);
        double price = houseDto.getPrice();
        house.setPrice(price);
        String currency = houseDto.getCurrency();
        if (!Objects.equals(currency, "EUR"))
            house.setPrice(currencyService.getPriceInEUR(currency, price));
        houseRepository.save(house);

        List<Picture> pictures = new ArrayList<>();

        for (MultipartFile file : images) {
            // Save image
            String filename = storageService.store(file, house.getId());

            Picture picture = pictureService.createHousePicture(filename, house);
            picture = pictureService.save(picture);
            pictures.add(picture);
        }

        house.setPictures(pictures);

        user.getHouses().add(house);
        userService.saveUser(user);

        return houseRepository.save(house);

    }

    @Override
    public House getHouseById(Long id) {
        Optional<House> house = houseRepository.findHouseToShow(id);

        if (house.isEmpty())
            throw new RuntimeException("House not found.");

        return house.get();
    }

    @Override
    public Set<House> getHouses() {
        return new HashSet<>(houseRepository.findAll());
    }

    @Override
    public List<House> getAllAvailableHouses(String currency) {
        return houseRepository.findByStatusAndEnabledOwners(HouseStatus.AVAILABLE);
    }

    @Override
    public List<HousePreviewDto> getHousesNearTo(House house, double km, String currency){
        GeoUtils.BoundingBox box = GeoUtils.getBoundingBox(house.getLatitude(), house.getLongitude(), km);
        List<House> houses = houseRepository.findInArea(
                box.minLat(), box.maxLat(), box.minLng(), box.maxLng());

        houses.removeIf(h -> h.getId().equals(house.getId()));

        return houses.stream().map(h -> new HousePreviewDto(h, currency)).toList();
    }

    @Override
    public List<HousePreviewDto> getFilteredHouses(String location, Double minPrice, Double maxPrice, Integer beds, Integer guests, HouseCategory category, String currency) {
        List<House> houses = houseRepository.findHousesByFilters(minPrice, maxPrice, beds, guests, category);

        if(location != null && !location.isEmpty()) {
            Object geoJson = geolocationService.getPolygonsFromLocation(location);
            houses = geoPolygonService.filterHousesInPolygon(geoJson, houses);
        }

        return houses.stream().map(h -> new HousePreviewDto(h, currency)).toList();
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
    public List<House> getHousesByOwnerId(Long ownerId) {
        return houseRepository.findHousesByOwnerId(ownerId);
    }

    @Override
    public Set<House> getHousesofLoggedUser(String token){
        return jwtService.getUser(token).getHouses();
    }

    @Override
    public boolean isMyHouse(String token, Long houseId) {
        User user = jwtService.getUser(token);
        List<Long> myHouses = houseRepository.findHousesIdByOwnerId(user.getId());

        return myHouses.contains(houseId);
    }

    @Override
    public House updateHouse(HouseUpdateDto houseDto, Long id, MultipartFile[] pictures) {
        House house = this.getHouseById(id);

        house.setTitle(houseDto.getTitle());
        house.setDescription(houseDto.getDescription());
        house.setGuests(houseDto.getGuests());
        house.setBedrooms(houseDto.getBedrooms());
        house.setBeds(houseDto.getBeds());
        house.setBathrooms(houseDto.getBathrooms());
        house.setCategory(HouseCategory.valueOf(houseDto.getCategory()));
        house.setStatus(HouseStatus.valueOf(houseDto.getStatus()));

        double price = houseDto.getPrice();
        house.setPrice(price);

        String currency = houseDto.getCurrency();

        if (!Objects.equals(currency, "EUR"))
            house.setPrice(currencyService.getPriceInEUR(currency, price));

        if(pictures!=null) {
            for(MultipartFile picture : pictures) {
                pictureService.updateHousePicture(picture, house);
            }
        }

        house.setLastUpdateDate(LocalDate.now());

        return houseRepository.save(house);
    }

    @Override
    public void deleteHouseById(Long id) {
        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty())
            throw new RuntimeException("House not found");

        for(Picture picture : house.get().getPictures()) {
            pictureService.delete(picture);
        }

        houseRepository.deleteById(id);
    }

    @Override
    public boolean isThatMyPictureHouse(Picture picture, User user){
        boolean result = false;
        List<House> houses = getHousesByOwnerId(user.getId());

        for(House house : houses){
            if (house.getPictures().contains(picture)) {
                result = true;
                break;
            }
        }

        return result;
    }
}

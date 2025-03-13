package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.entity.Rent;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.RentRepository;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service("rentService")
public class RentServiceImpl implements RentService {

    @Autowired
    @Qualifier("rentRepository")
    private RentRepository rentRepository;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public Rent saveRent(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public Rent createRent(String token, RentCreationDto dto){
        Rent rent = new Rent();

        House house = houseService.getHouseById(dto.getHouseId());
        house.setStatus(HouseStatus.RENTED);

        rent.setStartDate(dto.getStartTime());
        rent.setEndDate(dto.getEndTime());
        rent.setUser(jwtService.getUser(token));
        rent.setHouse(house);

        return this.saveRent(rent);
    }

    @Override
    public boolean isThatMyRentById(String token, long id) {
        return isthatMyRent(token, getRentById(id));
    }

    @Override
    public boolean isthatMyRent(String token, Rent rent) {
        Set<Rent> userRents = jwtService.getUser(token).getRents();
        return userRents.contains(rent);
    }

    @Override
    public Rent getRentById(long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        if(rent.isEmpty())
            throw new RuntimeException("Rent not found");

        return rent.get();
    }

    @Override
    public List<Rent> getRents() {
        return rentRepository.findAll();
    }

    @Override
    public Rent updateRent(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public boolean isRentActiveById(long id) {
        return isRentActive(this.getRentById(id));
    }

    @Override
    public boolean isRentActive(Rent rent) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(rent.getStartDate()) && now.isBefore(rent.getEndDate());
    }

    @Override
    public List<Rent> getActiveRentsByUser(long userId) {
        return rentRepository.findActiveRentsByUser(userId, LocalDateTime.now());
    }

    @Override
    public List<Rent> getActiveRentsLoggedUser(String token) {
        User user = jwtService.getUser(token);
        return rentRepository.findActiveRentsByUser(user.getId(), LocalDateTime.now());
    }

    @Override
    public Map<Long, List<RentDto>> getRentOfMyHouses(Set<House> houses){
        Map<Long, List<RentDto>> rents = new HashMap<>();

        for (House house : houses) {
            rents.put(house.getId(), house.getRents().stream().map(RentDto::new).toList());
        }

        return rents;
    }

    @Override
    public void cancelRent(String token, Rent rent) throws IllegalAccessException{
        if(!this.isRentActive(rent))
            throw new RuntimeException("Rent is not active");
        if(!isthatMyRent(token, rent))
            throw new IllegalAccessException("You are not authorize to cancel someone else rent");

        rent.setEndDate(LocalDateTime.now());
        this.saveRent(rent);
    }

    @Override
    public void cancelRentById(String token, long id) throws IllegalAccessException{
        this.cancelRent(token, this.getRentById(id));
    }

    @Override
    public void deleteRentById(long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        if(rent.isEmpty())
            throw new RuntimeException("Rent not found");

        rentRepository.deleteById(id);
    }
}

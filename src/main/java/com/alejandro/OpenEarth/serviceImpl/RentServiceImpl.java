package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.repository.RentRepository;
import com.alejandro.OpenEarth.service.EmailService;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service("rentService")
public class RentServiceImpl implements RentService {

    @Autowired
    @Qualifier("rentRepository")
    private RentRepository rentRepository;

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;

    @Override
    public Rent saveRent(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public Rent createRent(User user, RentCreationDto dto, Payment payment){
        Rent rent = new Rent();

        House house = houseService.getHouseById(dto.getHouseId());

        rent.setStartDate(dto.getStartTime());
        rent.setEndDate(dto.getEndTime());
        rent.setPrice(payment.getAmount());
        rent.setCancelled(false);
        rent.setUser(user);
        rent.setHouse(house);
        rent.setPayment(payment);

        this.saveRent(rent);
        this.houseService.save(house);
        emailService.rentedEmail(user.getEmail(), house.getTitle());

        return rent;
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
    public List<RentDto> getRentsToShow(Collection<Rent> rents) {
        return rents.stream().map(RentDto::new).toList();
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
        LocalDate now = LocalDate.now();
        return now.isBefore(rent.getStartDate());
    }

    @Override
    public Set<Rent> getActiveRentsByUser(long userId) {
        return rentRepository.findActiveRentsByUser(userId, LocalDateTime.now());
    }

    @Override
    public Set<Rent> getActiveRentsLoggedUser(String token) {
        User user = jwtService.getUser(token);
        return rentRepository.findActiveRentsByUser(user.getId(), LocalDateTime.now());
    }

    @Override
    public List<Rent> getRentsLoggedUser(String token) {
        User user = jwtService.getUser(token);
        return rentRepository.findRentsByUser(user);
    }

    @Override
    public List<Rent> getRentOfMyHouses(Set<House> houses){
        List<Rent> rent = new ArrayList<>();

        for (House house : houses) {
            rent.addAll(rentRepository.findRentsByHouse_Id(house.getId()));
        }

        return rent;
    }

    @Override
    public List<Rent> getRentsOfHouse(long houseId){
        return rentRepository.findRentsByHouse_Id(houseId);
    }

    @Override
    public void cancelRent(String token, Rent rent) throws IllegalAccessException{
        if(!this.isRentActive(rent))
            throw new RuntimeException("Rent is not pending");
        if(!isthatMyRent(token, rent))
            throw new IllegalAccessException("You are not authorized to cancel someone else rent");

        User user = jwtService.getUser(token);
        String title = rent.getHouse().getTitle();
        rent.setCancelled(true);
        rentRepository.save(rent);
        emailService.cancelEmail(user.getEmail(), title);
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

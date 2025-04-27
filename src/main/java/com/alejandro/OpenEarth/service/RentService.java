package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Payment;
import com.alejandro.OpenEarth.entity.Rent;
import com.alejandro.OpenEarth.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RentService{
    Rent saveRent(Rent rent);
    Rent createRent(User user, RentCreationDto rentDto, Payment payment);
    boolean isThatMyRentById(String token, long id);
    boolean isthatMyRent(String token, Rent rent);
    Rent getRentById(long id);
    List<Rent> getRents();
    List<RentDto> getRentsToShow(Collection<Rent> rents);
    Rent updateRent(Rent rent);
    boolean isRentActiveById(long id);
    boolean isRentActive(Rent rent);
    Set<Rent> getActiveRentsByUser(long userId);
    Set<Rent> getActiveRentsLoggedUser(String token);
    List<Rent> getRentsLoggedUser(String token);
    List<Rent> getRentOfMyHouses(Set<House> houses);
    List<Rent> getRentsOfHouse(long houseId);
    void cancelRentById(String token, long id) throws IllegalAccessException;
    void cancelRent(String token, Rent rent) throws IllegalAccessException;
    void deleteRentById(long id);
}

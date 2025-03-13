package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Rent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RentService{
    Rent saveRent(Rent rent);
    Rent createRent(String token, RentCreationDto rentDto);
    boolean isThatMyRentById(String token, long id);
    boolean isthatMyRent(String token, Rent rent);
    Rent getRentById(long id);
    List<Rent> getRents();
    Rent updateRent(Rent rent);
    boolean isRentActiveById(long id);
    boolean isRentActive(Rent rent);
    List<Rent> getActiveRentsByUser(long userId);
    List<Rent> getActiveRentsLoggedUser(String token);
    Map<Long, List<RentDto>> getRentOfMyHouses(Set<House> houses);
    void cancelRentById(String token, long id) throws IllegalAccessException;
    void cancelRent(String token, Rent rent) throws IllegalAccessException;
    void deleteRentById(long id);
}

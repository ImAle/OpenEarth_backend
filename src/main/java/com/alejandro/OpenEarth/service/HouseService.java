package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.House;

import java.util.Set;

public interface HouseService {
    House create(House house);
    House getHouseById(Long id);
    Set<House> getHouses();
    House updateHouse(House house);
    void deleteHouseById(Long id);
}

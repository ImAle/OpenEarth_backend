package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.Rent;

import java.util.List;

public interface RentService{
    Rent addRent(Rent rent);
    Rent getRentById(long id);
    List<Rent> getRents();
    Rent updateRent(Rent rent);
    void deleteRentById(long id);
}

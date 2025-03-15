package com.alejandro.OpenEarth.service;

public interface GeolocationService {

    Object getCoordinates(String country, String city, String street, String postalCode);
    Object getAddress(double latitude, double longitude);

}

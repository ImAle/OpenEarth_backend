package com.alejandro.OpenEarth.service;

public interface GeolocationService {

    Object getCoordinates(String country, String location);
    Object getAddress(double latitude, double longitude);
    Object getApiSearchResponse(String country, String location);
    Double[] getArea(String country, String location);

}

package com.alejandro.OpenEarth.service;

public interface GeolocationService {

    Object getCoordinates(String location);
    Object getAddress(double latitude, double longitude);
    Object getApiSearchResponse(String location);
    Double[] getArea(String location);

}

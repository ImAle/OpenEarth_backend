package com.alejandro.OpenEarth.service;

public interface GeolocationService {

    Object getCoordinates(String location);
    Object getPolygonsFromLocation(String location);
    Object getAddress(double latitude, double longitude);
    Object getSearchResponse(String location);
    Double[] getArea(String location);

}

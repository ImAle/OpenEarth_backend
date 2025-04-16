package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.House;

import java.util.List;

public interface GeoPolygonService {
    List<House> filterHousesInPolygon(Object geoJson, List<House> houses);

}

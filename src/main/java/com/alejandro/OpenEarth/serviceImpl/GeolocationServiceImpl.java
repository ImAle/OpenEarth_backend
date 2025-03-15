package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.GeolocationDto;
import com.alejandro.OpenEarth.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service("geolocationService")
public class GeolocationServiceImpl implements GeolocationService {

    @Autowired
    private RestTemplate restTemplate ;

    public Object getCoordinates(String country, String city, String street, String postalCode){
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("country", country)
                .queryParam("city", city)
                .queryParam("street", street)
                .queryParam("postalcode", postalCode)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        return restTemplate.getForObject(url, Object.class);
    }

    public Object getAddress(double latitude, double longitude){
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/reverse")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Object.class);
    }

}

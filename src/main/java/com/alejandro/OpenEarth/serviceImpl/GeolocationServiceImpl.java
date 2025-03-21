package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.GeolocationDto;
import com.alejandro.OpenEarth.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service("geolocationService")
public class GeolocationServiceImpl implements GeolocationService {

    @Autowired
    private RestTemplate restTemplate ;

    public Object getCoordinates(String country, String city, String street, String postalCode){
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("country", country.strip().replace(" ", "_"))
                .queryParam("city", city.strip().replace(" ", "_"))
                .queryParam("street", street.strip().replace(" ", "_"))
                .queryParam("postalcode", postalCode.strip())
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();


        ResponseEntity<Map[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Map[].class
        );

        Map<String, Object> geolocationResponse = response.getBody()[0];

        GeolocationDto dto = new GeolocationDto();
        dto.setLatitude((String) geolocationResponse.get("lat"));
        dto.setLongitude((String) geolocationResponse.get("lon"));
        dto.setDisplayName((String) geolocationResponse.get("display_name"));

        return dto;
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

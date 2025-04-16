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

    public Object getApiSearchResponse(String country, String location){
        String query = country.strip().replace(" ", "_") + "_" + location.strip().replace(" ", "_");
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();


        ResponseEntity<Map[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Map[].class
        );

        if (response.getBody() == null || response.getBody().length == 0) {
            throw new IllegalArgumentException("You need to provide a valid response. Whether it is from street, city, country or country, city, street");
        }

        return response.getBody()[0];
    }

    public Object getAddress(double latitude, double longitude){
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/reverse")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Object.class);
    }

    public Object getCoordinates(String country, String location){
        Object response = getApiSearchResponse(country, location);
        Map<String, Object> answer = (Map<String, Object>) response;

        GeolocationDto dto = new GeolocationDto();
        dto.setLatitude((String) answer.get("lat"));
        dto.setLongitude((String) answer.get("lon"));
        dto.setLocation((String) answer.get("display_name"));

        return dto;
    }

    public Double[] getArea(String country, String location){
        Object response = getApiSearchResponse(country, location);
        Map<String, Object> answer = (Map<String, Object>) response;

        Double[] coordinates = (Double[]) (answer.get("boundingbox"));

        return coordinates;
    }

}

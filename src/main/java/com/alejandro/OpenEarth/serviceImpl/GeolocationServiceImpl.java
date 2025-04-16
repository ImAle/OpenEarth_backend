package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.GeolocationDto;
import com.alejandro.OpenEarth.service.GeolocationService;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service("geolocationService")
public class GeolocationServiceImpl implements GeolocationService {

    @Autowired
    private RestTemplate restTemplate ;

    public Object getSearchResponse(String location){
        String query = location.strip().replace(" ", "_");
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

    public Object getPolygonsFromLocation(String location) {
        String query = location.strip().replace(" ", "_");
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("polygon_geojson", 1)
                .queryParam("limit", 1)
                .toUriString();

        ResponseEntity<Map[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Map[].class
        );

        if (response.getBody() == null || response.getBody().length == 0) {
            throw new IllegalArgumentException("No results from Nominatim.");
        }

        return response.getBody()[0].get("geojson");
    }

    public Object getAddress(double latitude, double longitude){
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/reverse")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Object.class);
    }

    public Object getCoordinates(String location){
        Object response = getSearchResponse(location);
        Map<String, Object> answer = (Map<String, Object>) response;

        GeolocationDto dto = new GeolocationDto();
        dto.setLatitude((String) answer.get("lat"));
        dto.setLongitude((String) answer.get("lon"));
        dto.setLocation((String) answer.get("display_name"));

        return dto;
    }

    public Double[] getArea(String location){
        Object response = getSearchResponse(location);
        Map<String, Object> answer = (Map<String, Object>) response;

        List<String> coordinates = (List<String>) answer.get("boundingbox");

        return coordinates.stream()
                .map(Double::parseDouble)
                .toArray(Double[]::new);
    }

}

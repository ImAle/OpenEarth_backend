package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.GeolocationDto;
import com.alejandro.OpenEarth.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geo")
public class GeolocationController {

    @Autowired
    @Qualifier("geolocationService")
    private GeolocationService geolocationService;

    @GetMapping("/search")
    public ResponseEntity<?> getCoordinates(@RequestParam("country") String country, @RequestParam("city") String city, @RequestParam("street") String street, @RequestParam("postalCode") String postalCode) {
        Object geolocationDto = geolocationService.getCoordinates(country, city, street, postalCode);

        if (geolocationDto == null)
            return new ResponseEntity<>("No geolocation found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(geolocationDto, HttpStatus.OK);
    }

    @GetMapping("/reverse")
    public ResponseEntity<?> getReverseGeolocation(@RequestParam double lat, @RequestParam double lon) {
        Object response = geolocationService.getAddress(lat, lon);

        if(response == null)
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

        return ResponseEntity.ok().body(response);
    }
}

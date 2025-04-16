package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geo")
public class GeolocationController {

    @Autowired
    @Qualifier("geolocationService")
    private GeolocationService geolocationService;

    @GetMapping("/search")
    public ResponseEntity<?> getCoordinates(@RequestParam("location") String location) {
        try{
            Object geolocationDto = geolocationService.getCoordinates(location);

            if (geolocationDto == null)
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

            return ResponseEntity.ok().body(geolocationDto);
        }catch (IllegalArgumentException iaex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", iaex.getMessage()));
        }

    }

    @GetMapping("/reverse")
    public ResponseEntity<?> getReverseGeolocation(@RequestParam double lat, @RequestParam double lon) {
        Object response = geolocationService.getAddress(lat, lon);

        if(response == null)
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

        return ResponseEntity.ok().body(response);
    }
}

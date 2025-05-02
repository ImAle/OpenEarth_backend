package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Rent;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.RentService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    @Autowired
    @Qualifier("rentService")
    private RentService rentService;

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @GetMapping("/myRents")
    public ResponseEntity<?> getMyRents(@RequestHeader("Authorization") String token) {
        try{
            List<Rent> myRents = rentService.getRentsLoggedUser(token);

            if (myRents.isEmpty())
                return ResponseEntity.noContent().build();

            List<RentDto> dtos = rentService.getRentsToShow(myRents);

            return ResponseEntity.ok().body(Map.of("rents", dtos));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rtex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/house")
    public ResponseEntity<?> getRentOfMyHouse(@RequestParam("id") Long houseId) {
        try{
            List<Rent> rents = rentService.getRentsOfHouse(houseId);

            if(rents.isEmpty())
                return ResponseEntity.noContent().build();

            List<RentDto> dtos = rentService.getRentsToShow(rents);

            return ResponseEntity.ok().body(Map.of("rents", dtos));
        }catch (RuntimeException rtex){
            rtex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rtex.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/houses")
    public ResponseEntity<?> getRentsOfMyHouses(@RequestHeader("Authorization") String token){
        try{
            Set<House> houses = houseService.getHousesofLoggedUser(token);
            List<Rent> rents = rentService.getRentOfMyHouses(houses);

            if(rents.isEmpty())
                return ResponseEntity.noContent().build();

            List<RentDto> dtos = rentService.getRentsToShow(rents);

            return ResponseEntity.ok().body(Map.of("rents", dtos));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rtex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelRent(@RequestHeader("Authorization") String token, @RequestParam Long rentId) {
        try{
            if(!jwtService.isGuest(token))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allowed to perform this operation");

            rentService.cancelRentById(token, rentId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Rent has been cancelled"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtex.getMessage());
        }catch (IllegalAccessException iaex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(iaex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

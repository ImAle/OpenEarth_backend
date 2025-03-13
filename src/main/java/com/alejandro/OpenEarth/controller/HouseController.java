package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.*;
import com.alejandro.OpenEarth.entity.Country;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.upload.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createHouse(@RequestHeader("Authorization") String token, @Valid @RequestBody HouseCreationDto houseDto, BindingResult result) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            House house = houseService.create(token, houseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("house", house));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getHouses(@RequestParam(required = false) Country country, @RequestParam(required = false) Double minPrice,
                                       @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Integer beds,
                                       @RequestParam(required = false) Integer guests, @RequestParam(required = false) HouseCategory category) {

        List<HousePreviewDto> houses = houseService.getFilteredHouses(country, minPrice, maxPrice, beds, guests, category);

        if(houses.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok().body(Map.of("houses", houses));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        HouseCategory[] categories = houseService.getHouseCategories();
        if(categories.length == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok().body(Map.of("categories", categories));
    }

    @GetMapping("/details")
    public ResponseEntity<?> getHouse(@RequestHeader("Authorization") String token, @RequestParam("id") Long houseId) {
        try{
            House house = houseService.getHouseById(houseId);

            // if the owner tries to see his own house
            if(houseService.isMyHouse(token, houseId)){
                return ResponseEntity.ok().body(Map.of("house", new HouseUpdateFormDto(house)));
            }

            return ResponseEntity.ok().body(Map.of("house", new HouseDetailsDto(house)));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateHouse(@RequestHeader("Authorization") String token, @Valid @RequestBody HouseUpdateDto houseDto, BindingResult result, @RequestParam("id") Long id) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            if(!houseService.isMyHouse(token, id))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not edit this house"));

            House house = houseService.updateHouse(houseDto, id);

            return ResponseEntity.ok().body(Map.of("house", house));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHouse(@RequestHeader("Authorization") String token, @RequestParam("id") Long houseId) {
        try{
            if(!houseService.isMyHouse(token, houseId))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not edit this house"));

            houseService.deleteHouseById(houseId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}

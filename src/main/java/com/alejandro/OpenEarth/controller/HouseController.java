package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import com.alejandro.OpenEarth.dto.HousePreviewDto;
import com.alejandro.OpenEarth.dto.HouseDetailsDto;
import com.alejandro.OpenEarth.dto.HouseUpdateDto;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("fileService")
    private StorageService storageService;

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createHouse(@RequestBody HouseCreationDto houseDto) {
        try{
            House house = houseService.create(houseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("house", house));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getHouses() {
        List<HousePreviewDto> houses = houseService.getAllAvailableHouses();
        return ResponseEntity.ok().body(Map.of("houses", houses));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        HouseCategory[] categories = houseService.getHouseCategories();
        return ResponseEntity.ok().body(Map.of("categories", categories));
    }

    @GetMapping("/details")
    public ResponseEntity<?> getHouse(@RequestHeader("Authorization") String token, @RequestParam("id") Long houseId) {
        try{
            House house = houseService.getHouseById(houseId);

            // if the owner tries to see his own house
            if(houseService.isMyHouse(token, houseId)){
                HouseUpdateDto houseUpdateDto = new HouseUpdateDto();
                return ResponseEntity.ok().body(Map.of("house", houseUpdateDto.fromEntityToDto(house)));
            }

            return ResponseEntity.ok().body(Map.of("house", new HouseDetailsDto(house)));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateHouse(@RequestHeader("Authorization") String token, @RequestBody HouseUpdateDto houseDto) {
        try{
            if(!houseService.isMyHouse(token, houseDto.getId()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not edit this house"));

            HouseUpdateDto houseUpdateDto = new HouseUpdateDto();
            House house = houseUpdateDto.fromDtoToEntity(houseDto);
            houseService.updateHouse(house);

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

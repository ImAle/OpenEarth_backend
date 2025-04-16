package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.*;
import com.alejandro.OpenEarth.entity.Country;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.HouseCategory;
import com.alejandro.OpenEarth.entity.HouseStatus;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.serviceImpl.CurrencyServiceImpl;
import com.alejandro.OpenEarth.upload.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("currencyService")
    private CurrencyServiceImpl currencyService;

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createHouse(@RequestHeader("Authorization") String token, @Valid @RequestPart("house") HouseCreationDto houseDto, BindingResult result, @RequestPart("pictures") MultipartFile[] pictures) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            if(pictures.length == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", "You must provide at least one picture"));

            houseService.create(token, houseDto, pictures);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "house registered successfully"));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getHouses(@RequestParam(required = false) String location, @RequestParam(required = false) Country country,
                                       @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
                                       @RequestParam(required = false) Integer beds, @RequestParam(required = false) Integer guests,
                                       @RequestParam(required = false) HouseCategory category, @RequestParam(required = false) String currency) {

        if(currency == null || currency.isEmpty())
            currency = "EUR";

        if (!currency.equals("EUR")){
            if(maxPrice != null && maxPrice > 0)
                maxPrice = currencyService.getPriceInEUR(currency, maxPrice);
            if(minPrice != null && minPrice > 0)
                minPrice = currencyService.getPriceInEUR(currency, minPrice);
        }

        List<HousePreviewDto> houses = houseService.getFilteredHouses(country, location, minPrice, maxPrice, beds, guests, category, currency);

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

    @GetMapping("/status")
    public ResponseEntity<?> getStatuses(){
        HouseStatus[] statuses = houseService.getHouseStatuses();
        if(statuses.length == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok().body(Map.of("statuses", statuses));
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries() {
        String[] countries = houseService.getCountries();
        if(countries.length == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok().body(Map.of("countries", countries));
    }

    @GetMapping("/details")
    public ResponseEntity<?> getHouse(@RequestParam("id") Long houseId, @RequestParam(value = "currency", required = false) String currency) {
        try{
            if(currency == null || currency.isEmpty())
                currency = "EUR";

            House house = houseService.getHouseById(houseId);

            HouseDetailsDto dto = new HouseDetailsDto(house, currency);

            return ResponseEntity.ok().body(Map.of("house", dto));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateHouse(@RequestHeader("Authorization") String token, @Valid @RequestBody HouseUpdateDto houseDto, BindingResult result, @RequestParam("id") Long id, @RequestParam("pictures") List<MultipartFile> newPictures) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            if(!houseService.isMyHouse(token, id))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not edit this house"));

            House house = houseService.updateHouse(houseDto, id, newPictures);

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

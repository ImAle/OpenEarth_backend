package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.*;
import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.ReviewService;
import com.alejandro.OpenEarth.serviceImpl.CurrencyServiceImpl;
import com.alejandro.OpenEarth.serviceImpl.UserService;
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
    @Qualifier("reviewService")
    private ReviewService reviewService;

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
    public ResponseEntity<?> getHouses(@RequestParam(required = false) String location,
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

        List<HousePreviewDto> houses = houseService.getFilteredHouses(location, minPrice, maxPrice, beds, guests, category, currency);

        if(houses.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok().body(Map.of("houses", houses));
    }

    @GetMapping("/nearTo")
    public ResponseEntity<?> getHousesNearTo(@RequestParam("id") Long id, @RequestParam("km") double km, @RequestParam(value = "currency", required = false) String currency){
        try{
            House house = houseService.getHouseById(id);

            if(currency == null)
                currency = "EUR";

            List<HousePreviewDto> houses = houseService.getHousesNearTo(house, km, currency);

            if(houses.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.ok().body(Map.of("houses", houses));
        }catch(RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getHousesByOwner(@RequestParam("owner") long id, @RequestParam(value = "currency", required = false) String currency){
        try{
            System.out.println("Owner: " + id);
            List<House> houses = houseService.getHousesByOwnerId(id);

            if(houses.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


            if(currency == null || currency.isEmpty())
                currency = "EUR";

            String finalCurrency = currency;
            List<HousePreviewDto> preview = houses.stream().map(house -> new HousePreviewDto(house, finalCurrency)).toList();

            return ResponseEntity.ok().body(Map.of("houses", preview));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
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

    @PutMapping(path = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateHouse(@RequestHeader("Authorization") String token, @Valid @RequestPart("house") HouseUpdateDto houseDto, BindingResult result, @RequestParam("id") Long id, @RequestPart(value = "pictures", required = false) MultipartFile[] newPictures) {
        try{
            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            if(!houseService.isMyHouse(token, id))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not edit this house"));

            houseService.updateHouse(houseDto, id, newPictures);

            return ResponseEntity.ok().body(Map.of("message", "This house has been updated successfully"));
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You can not delete this house"));

            houseService.deleteHouseById(houseId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (RuntimeException rtex){
            rtex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}

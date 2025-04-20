package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.PictureService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired
    @Qualifier("pictureService")
    private PictureService pictureService;

    @Autowired
    @Qualifier("houseService")
    private HouseService houseService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePicture(@RequestHeader("Authorization") String token, @RequestParam("id") long id) {
        try{
            User user = jwtService.getUser(token);
            Picture picture = pictureService.getPictureById(id);

            if(!houseService.isThatMyPictureHouse(picture, user))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You don't have permission to delete this picture"));

            pictureService.delete(picture);
            return ResponseEntity.ok().body(Map.of("message", "Successfully deleted"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}

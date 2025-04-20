package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    Picture save(Picture picture);
    Picture createHousePicture(String filename, House house);
    Picture createUserPicture(String filename, User user);
    Picture getPictureById(Long id);
    void delete(Picture picture);
    void updateUserPicture(MultipartFile dtoPicture, User user);
    void updateHousePicture(MultipartFile dtoPicture, House house);
    boolean isThatMyPicture(Picture picture, User user);
}

package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.configuration.PictureConfiguration;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.HouseRepository;
import com.alejandro.OpenEarth.repository.PictureRepository;
import com.alejandro.OpenEarth.service.HouseService;
import com.alejandro.OpenEarth.service.PictureService;
import com.alejandro.OpenEarth.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    @Autowired
    @Qualifier("pictureConfiguration")
    private PictureConfiguration pictureConfiguration;

    @Autowired
    @Qualifier("pictureRepository")
    private PictureRepository pictureRepository;

    @Autowired
    @Qualifier("houseRepository")
    private HouseRepository houseRepository;

    @Autowired
    @Qualifier("fileService")
    private StorageService storageService;

    @Override
    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture createHousePicture(String filename, House house) {
        return new Picture(pictureConfiguration.getUrlPrefix() + filename, house);
    }

    @Override
    public Picture createUserPicture(String filename, User user) {
        delete(user.getPicture());
        return new Picture(pictureConfiguration.getUrlPrefix() + filename, user);
    }

    @Override
    public Picture getPictureById(Long id) {
        Optional<Picture> picture = pictureRepository.findById(id);
        if(picture.isEmpty())
            throw new RuntimeException("Picture not found");
        return picture.get();
    }

    @Override
    public void delete(Picture picture) {
        if (picture != null) {
            String filename = picture.getUrl().substring(pictureConfiguration.getUrlPrefix().length());
            try {
                House house = picture.getHouse();
                house.getPictures().remove(picture);
                houseRepository.save(house);
                storageService.delete(filename); // Deletes image from the storage
                pictureRepository.delete(picture);
            } catch (IOException e) {
                System.err.println("Error deleting picture: " + e.getMessage());
            }
        }
    }

    @Override
    public void updateUserPicture(MultipartFile dtoPicture, User user){
        String filename = storageService.store(dtoPicture, user.getId());
        Picture picture = this.createUserPicture(filename, user);
        this.save(picture);
        user.setPicture(picture);
    }

    @Override
    public void updateHousePicture(MultipartFile dtoPicture, House house) {
        String filename = storageService.store(dtoPicture, Long.getLong(house.getOwner().getId() + "" + house.getId()));
        Picture picture = this.createHousePicture(filename, house);
        this.save(picture);
        house.getPictures().add(picture);
    }

    @Override
    public boolean isThatMyPicture(Picture picture, User user){
        System.out.println("Picture id: " + picture.getId());
        return picture.getUser() == user;
    }
}

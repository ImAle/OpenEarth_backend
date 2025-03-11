package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.configuration.PictureConfiguration;
import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.PictureRepository;
import com.alejandro.OpenEarth.service.PictureService;
import com.alejandro.OpenEarth.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    @Autowired
    @Qualifier("pictureConfiguration")
    private PictureConfiguration pictureConfiguration;

    @Autowired
    @Qualifier("pictureRepository")
    private PictureRepository pictureRepository;

    @Autowired
    @Qualifier("fileService")
    private StorageService storageService;

    @Override
    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture createHousePicture(String filename, House house) {
        Picture picture = new Picture();
        picture.setUrl(pictureConfiguration.getUrlPrefix() + filename);
        picture.setHouse(house);
        picture.setUser(null);
        return picture;
    }

    @Override
    public void delete(Picture picture) {
        if (picture != null) {
            String filename = picture.getUrl().substring(pictureConfiguration.getUrlPrefix().length());
            try {
                storageService.delete(filename); // Deletes image form the storage
                pictureRepository.delete(picture);
            } catch (IOException e) {
                System.err.println("Error deleting picture: " + e.getMessage());
            }
        }
    }

    @Override
    public void updateUserPicture(MultipartFile dtoPicture, User user){
        String filename = storageService.store(dtoPicture, user.getId());
        Picture picture = new Picture();
        picture.setUrl(pictureConfiguration.getUrlPrefix() + filename);
        picture.setUser(user);
        picture.setHouse(null);
        this.save(picture);
        user.setPicture(picture);
    }


}

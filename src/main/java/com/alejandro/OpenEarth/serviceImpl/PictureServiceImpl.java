package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.Picture;
import com.alejandro.OpenEarth.repository.PictureRepository;
import com.alejandro.OpenEarth.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    @Autowired
    @Qualifier("pictureRepository")
    private PictureRepository pictureRepository;

    @Override
    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }
}

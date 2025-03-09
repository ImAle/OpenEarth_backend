package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pictureRepository")
public interface PictureRepository extends JpaRepository<Picture, Long> {

}

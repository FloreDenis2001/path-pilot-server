package com.mycode.pathpilotserver.image.repository;

import com.mycode.pathpilotserver.image.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {

    Optional<Image> findImageByUserEmail(String email);
}

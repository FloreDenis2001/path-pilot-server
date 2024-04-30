package com.mycode.pathpilotserver.image.services;

import com.mycode.pathpilotserver.image.models.Image;

import java.util.Optional;

public interface ImageServiceQuerry {
    String findImageByUserAfterEmail(String email);
}

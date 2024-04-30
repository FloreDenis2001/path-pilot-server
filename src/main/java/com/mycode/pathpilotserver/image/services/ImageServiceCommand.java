package com.mycode.pathpilotserver.image.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceCommand {


    byte[] downloadImage(String fileName);
}

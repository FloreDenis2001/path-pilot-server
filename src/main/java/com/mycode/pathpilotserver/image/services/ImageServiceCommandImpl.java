package com.mycode.pathpilotserver.image.services;

import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.image.repository.ImageRepo;
import com.mycode.pathpilotserver.image.utils.ImageUtils;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
@Service
@Transactional
public class ImageServiceCommandImpl implements ImageServiceCommand{

    private final ImageRepo imageRepo;


    public ImageServiceCommandImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;

    }



    @Override
    public byte[] downloadImage(String fileName) {
        Optional<Image> dbImageData = imageRepo.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getData());
        return images;
    }


}

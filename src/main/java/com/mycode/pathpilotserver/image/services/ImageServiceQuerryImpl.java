package com.mycode.pathpilotserver.image.services;

import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.image.repository.ImageRepo;
import com.mycode.pathpilotserver.image.utils.ImageUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceQuerryImpl implements ImageServiceQuerry{
    private final ImageRepo imageRepo;

    public ImageServiceQuerryImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Override
    public String findImageByUserAfterEmail(String email) {
        Optional<Image> dbImageData = imageRepo.findImageByUserEmail(email);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getData());
        String base64Image = ImageUtils.encodeToString(images);
        return base64Image;
    }
}

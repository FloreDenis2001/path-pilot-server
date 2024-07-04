package com.mycode.pathpilotserver.image.services;

import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.image.repository.ImageRepo;
import com.mycode.pathpilotserver.image.utils.ImageUtils;
import com.mycode.pathpilotserver.user.exceptions.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceQuerryImpl implements ImageServiceQuerry {
    private final ImageRepo imageRepo;

    public ImageServiceQuerryImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Override
    public String findImageByUserAfterEmail(String email) {
        Optional<Image> dbImageData = imageRepo.findImageByUserEmail(email);
        if (dbImageData.isEmpty()) {
            System.out.println("No image found for user with email: " + email);
            return null;
        }

        byte[] data = dbImageData.get().getData();
        if (data == null) {
            System.out.println("Image data is null for user with email: " + email);
            return null;
        }

        try {
            byte[] images = ImageUtils.decompressImage(data);
            return ImageUtils.encodeToString(images);
        } catch (IOException e) {

            throw new UnauthorizedAccessException("Failed to decompress image");
        }

    }
}

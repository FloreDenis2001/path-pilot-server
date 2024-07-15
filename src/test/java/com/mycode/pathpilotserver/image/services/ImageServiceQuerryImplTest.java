package com.mycode.pathpilotserver.image.services;

import com.mycode.pathpilotserver.image.exceptions.ImageNotFoundException;
import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.image.repository.ImageRepo;
import com.mycode.pathpilotserver.image.utils.ImageUtils;
import com.mycode.pathpilotserver.user.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceQuerryImplTest {

    @Mock
    private ImageRepo imageRepo;

    @InjectMocks
    private ImageServiceQuerryImpl imageServiceQuerry;


    private byte[] compressToGzip(byte[] data) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
            gzip.flush();
            return bos.toByteArray();
        }
    }



    @Test
    void testFindImageByUserAfterEmailWhenImageDoesNotExist() {
        String email = "user@example.com";

        when(imageRepo.findImageByUserEmail(email)).thenReturn(Optional.empty());

        ImageNotFoundException thrown = assertThrows(ImageNotFoundException.class, () -> imageServiceQuerry.findImageByUserAfterEmail(email));
        assertEquals("Image not found", thrown.getMessage());
    }

    @Test
    void testFindImageByUserAfterEmailWhenImageDataIsNull() throws IOException {
        String email = "user@example.com";
        Image image = new Image();
        image.setData(null);

        when(imageRepo.findImageByUserEmail(email)).thenReturn(Optional.of(image));

        ImageNotFoundException thrown = assertThrows(ImageNotFoundException.class, () -> imageServiceQuerry.findImageByUserAfterEmail(email));
        assertEquals("Image data is null", thrown.getMessage());
    }

}

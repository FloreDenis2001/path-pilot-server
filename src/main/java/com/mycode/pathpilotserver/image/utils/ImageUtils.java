package com.mycode.pathpilotserver.image.utils;

import com.mycode.pathpilotserver.user.exceptions.UnauthorizedAccessException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ImageUtils {

    public static byte[] compressImage(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
            gzipOutputStream.finish();
        } catch (IOException e) {
            throw new UnauthorizedAccessException("Failed to compress image");
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new UnauthorizedAccessException("Failed to decompress image: " + e.getMessage());
        }
    }

    public static String encodeToString(byte[] images) {
        return Base64.getEncoder().encodeToString(images);
    }
}

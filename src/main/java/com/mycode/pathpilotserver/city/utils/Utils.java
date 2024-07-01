package com.mycode.pathpilotserver.city.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.packages.services.PackageCommandServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<City> readCitiesFromJsonFile() throws IOException {
        ClassLoader classLoader = PackageCommandServiceImpl.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("ro.json")) {
            if (inputStream == null) {
                throw new FileNotFoundException("ro.json file not found in resources");
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<City>>() {});
        }
    }

}

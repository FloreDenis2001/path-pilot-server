package com.mycode.pathpilotserver.intercom.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.mycode.pathpilotserver.utils.Utile.API_KEY_GOOGLE;


@Service
public class DirectionsService implements IDirections {
    private final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY_GOOGLE).build();

    @Override
    public DirectionsResult getDirections(String origin, String destination) throws IOException, InterruptedException, ApiException {
        return DirectionsApi.newRequest(context)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.DRIVING)
                .await();
    }





}

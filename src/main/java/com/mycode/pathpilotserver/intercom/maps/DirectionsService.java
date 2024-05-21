package com.mycode.pathpilotserver.intercom.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.mycode.pathpilotserver.utils.Utile.API_KEY;

@Service
public class DirectionsService implements IDirections {
    private final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    @Override
    public DirectionsResult getDirections(String origin, String destination) throws IOException, InterruptedException, ApiException {
        return DirectionsApi.newRequest(context)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC).await();
    }

    @Override
    public long getDistanceInMeters(String origin, String destination) throws IOException, InterruptedException, ApiException {
        DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                .origins(origin)
                .destinations(destination)
                .mode(TravelMode.DRIVING)
                .await();

        if (result.rows.length > 0 &&
                result.rows[0].elements.length > 0 &&
                result.rows[0].elements[0].status == DistanceMatrixElementStatus.OK) {
            return result.rows[0].elements[0].distance.inMeters;
        } else {
            String errorMessage = "Unable to calculate distance: " + result.rows[0].elements[0].status;
            throw new IOException(errorMessage);
        }
    }


}

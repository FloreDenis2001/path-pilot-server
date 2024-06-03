package com.mycode.pathpilotserver.intercom.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;

public interface IDirections {
   DirectionsResult getDirections(String origin, String destination) throws IOException, InterruptedException, ApiException;
}

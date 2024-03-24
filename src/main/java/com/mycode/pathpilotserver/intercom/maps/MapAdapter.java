package com.mycode.pathpilotserver.intercom.maps;

import com.mycode.pathpilotserver.address.Address;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MapAdapter {
    private final MapClient mapClient;

    public MapAdapter(MapClient mapClient) {
        this.mapClient = mapClient;
    }

    public ResponseEntity<JSONObject> getRoute(Address origin, Address destination, String key) {
        if (origin == null || destination == null || key == null) {
            throw new IllegalArgumentException("Origin, destination and key must not be null");
        }
        return mapClient.getRoute(origin, destination, key);
    }
}

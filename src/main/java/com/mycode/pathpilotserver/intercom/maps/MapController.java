package com.mycode.pathpilotserver.intercom.maps;


import com.mycode.pathpilotserver.address.Address;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maps")
@CrossOrigin
@Slf4j
public class MapController {

    private final MapAdapter mapAdapter;

    public MapController(MapAdapter mapAdapter) {
        this.mapAdapter = mapAdapter;
    }

    @GetMapping("/getRoute")
    public ResponseEntity<JSONObject> getRoute(@RequestParam("origin")Address origin, @RequestParam("destination") Address destination,@RequestParam("key")String key) {
        return mapAdapter.getRoute(origin, destination, key);
    }
}

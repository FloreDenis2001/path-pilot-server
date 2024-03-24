package com.mycode.pathpilotserver.intercom.maps;
import org.springframework.cloud.openfeign.FeignClient;
import com.mycode.pathpilotserver.address.Address;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MapClient", url = "https://maps.googleapis.com/maps/api/directions")
public interface MapClient {

    @GetMapping("/json")
    ResponseEntity<JSONObject> getRoute(
            @RequestParam("origin")Address origin,
            @RequestParam("destination") Address destination,
            @RequestParam("key") String key
    );
}

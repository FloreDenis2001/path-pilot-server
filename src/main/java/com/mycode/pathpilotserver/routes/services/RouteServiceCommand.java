package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.city.models.City;
import org.json.JSONException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface RouteServiceCommand {
    @Transactional
    void generateRoute(String companyRegistrationNumber, String city) throws JSONException, IOException;
}

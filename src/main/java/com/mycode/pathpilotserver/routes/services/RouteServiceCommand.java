package com.mycode.pathpilotserver.routes.services;

import org.json.JSONException;

import java.io.IOException;

public interface RouteServiceCommand {
    void generateRoute(String companyRegistrationNumber) throws JSONException, IOException;
}

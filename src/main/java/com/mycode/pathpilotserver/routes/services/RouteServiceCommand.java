package com.mycode.pathpilotserver.routes.services;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface RouteServiceCommand {
    void generateRoute(String companyRegistrationNumber) throws JSONException, IOException;
}

package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.routes.repository.RouteRepo;

public class RouteServiceQuerryImpl implements RouteServiceQuerry {
    private final RouteRepo routeRepo;

    public RouteServiceQuerryImpl(RouteRepo routeRepo) {
        this.routeRepo = routeRepo;
    }




}

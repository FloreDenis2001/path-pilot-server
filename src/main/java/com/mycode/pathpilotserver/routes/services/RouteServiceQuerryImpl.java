package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.routes.dto.RouteDTO;
import com.mycode.pathpilotserver.routes.exceptions.RouteNotFoundException;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;
import com.mycode.pathpilotserver.utils.Convertor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RouteServiceQuerryImpl implements RouteServiceQuerry {
    private final RouteRepo routeRepo;

    public RouteServiceQuerryImpl(RouteRepo routeRepo) {
        this.routeRepo = routeRepo;
    }


    @Override
    public Optional<List<RouteDTO>> findAllByCompanyRegistrationNumber(String companyRegistrationNumber) {
        Optional<List<Route>> routes = routeRepo.findAllByCompanyRegistrationNumber(companyRegistrationNumber);
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found for company with registration number: " + companyRegistrationNumber);
        }

        return Convertor.convertRouteToRouteDTO(routes.get());
    }
}

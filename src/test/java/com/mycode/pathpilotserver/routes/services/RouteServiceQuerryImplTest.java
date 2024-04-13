package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.routes.exceptions.ShipmentDetailsNotFoundException;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.routes.repository.RouteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RouteServiceQuerryImplTest {

    @Mock
    private RouteRepo routeRepo;

    private RouteServiceQuerry routeServiceQuerry;

    @BeforeEach
    void setUp() {
        routeServiceQuerry = new RouteServiceQuerryImpl(routeRepo);
    }

    private Route createRoute() {
        Route route = new Route();
        route.setId(1L);
        route.setArrivalTime(LocalDateTime.of(2021, 10, 10, 10, 10));
        return route;
    }


}
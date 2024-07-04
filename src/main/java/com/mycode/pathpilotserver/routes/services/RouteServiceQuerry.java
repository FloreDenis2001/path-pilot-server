package com.mycode.pathpilotserver.routes.services;

import com.mycode.pathpilotserver.routes.dto.RouteDTO;

import java.util.List;
import java.util.Optional;


public interface RouteServiceQuerry {


        Optional<List<RouteDTO>> findAllByCompanyRegistrationNumber(String companyRegistrationNumber);


}

package com.mycode.pathpilotserver.customers.services;

import com.mycode.pathpilotserver.customers.dto.CustomerCreateRequest;
import com.mycode.pathpilotserver.customers.dto.RemoveValidationRequest;
import org.springframework.stereotype.Service;

@Service
public interface CustomerCommandService {

    void create(CustomerCreateRequest customerCreateRequest);

    void delete(RemoveValidationRequest removeValidationRequest);
}

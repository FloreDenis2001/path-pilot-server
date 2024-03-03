package com.mycode.pathpilotserver.vehicles.services;

import com.mycode.pathpilotserver.vehicles.dto.CreateVehicleRequest;
import com.mycode.pathpilotserver.vehicles.dto.UpdatedVehicleRequest;

public interface VehicleServiceCommand {
        void create(CreateVehicleRequest createVehicleRequest);

        void update(UpdatedVehicleRequest updatedVehicleRequest);

        void delete(String registrationNumber);
}

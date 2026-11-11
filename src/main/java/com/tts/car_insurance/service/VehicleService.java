package com.tts.car_insurance.service;

import com.tts.car_insurance.dto.request.VehicleRequest;
import com.tts.car_insurance.dto.response.VehicleResponse;

public interface VehicleService {
    VehicleResponse save(String userId,VehicleRequest request);

    void validateRepairCost(String vehicleId, double repairCost);
    VehicleResponse getVehicleAndVerifyOwnership(String vehicleId, String userId);
}

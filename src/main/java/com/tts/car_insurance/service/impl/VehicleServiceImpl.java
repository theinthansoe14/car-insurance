package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.dto.request.VehicleRequest;
import com.tts.car_insurance.dto.response.VehicleResponse;
import com.tts.car_insurance.exception.impl.ExceedsVehicleValueException;
import com.tts.car_insurance.exception.impl.LicensePlateAlreadyExistException;
import com.tts.car_insurance.exception.impl.VehicleNotFoundException;
import com.tts.car_insurance.model.Vehicle;
import com.tts.car_insurance.repository.VehicleRepository;
import com.tts.car_insurance.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    private static final String CACHE_PREFIX = "vehicle:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    @Override
    public VehicleResponse save(String userId,VehicleRequest request) {

        Vehicle vehicle = toEntity(request);
        vehicle.setUserId(userId);

        try {
            Vehicle savedVehicle = vehicleRepository.save(vehicle);

            VehicleResponse vehicleResponse = toResponse(savedVehicle);

            redisTemplate.opsForValue().set(CACHE_PREFIX + vehicleResponse.id(), vehicleResponse, CACHE_TTL);

            return vehicleResponse;
        }catch (DuplicateKeyException e){
            throw new LicensePlateAlreadyExistException("LicensePlate :"+request.licensePlate()+" is already exist");
        }
    }

    @Override
    public void validateRepairCost(String vehicleId, double repairCost) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));

        if (repairCost > vehicle.getCarValue()) {
            throw new ExceedsVehicleValueException(
                    "Repair cost (" + repairCost +
                            ") exceeds the maximum car value limit (" + vehicle.getCarValue() + ")"
            );
        }
    }
    @PostAuthorize("hasRole('ADMIN') or returnObject.userId == #userId")
    public VehicleResponse getVehicleAndVerifyOwnership(String vehicleId, String userId) {

        String cacheKey = CACHE_PREFIX + vehicleId;

        VehicleResponse cachedVehicle = (VehicleResponse) redisTemplate.opsForValue().get(cacheKey);

        if (cachedVehicle != null) {
            return cachedVehicle;
        }

       Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()->new VehicleNotFoundException("Vehicle not found with id :"+vehicleId));

        VehicleResponse vehicleResponse = toResponse(vehicle);

        redisTemplate.opsForValue().set(cacheKey , vehicleResponse , CACHE_TTL);

        return vehicleResponse;

    }

    private Vehicle toEntity(VehicleRequest request){

        return Vehicle.builder()
                .licensePlate(request.licensePlate())
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .carValue(request.carValue())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private VehicleResponse toResponse(Vehicle vehicle){

        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getUserId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getCarValue(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }

}

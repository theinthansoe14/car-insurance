package com.tts.car_insurance.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record VehicleResponse(
        String id,
        String userId,
        String licensePlate,
        String brand,
        String model,
        int year,
        double carValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {}
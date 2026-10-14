package com.tts.car_insurance.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PolicyResponse(
        String id,
        String policyNumber,
        String userId,
        String vehicleId,
        LocalDate startDate,
        LocalDate endDate,
        double premiumAmount,
        String status,
        boolean hasClaim,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)implements Serializable {}
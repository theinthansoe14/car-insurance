package com.tts.car_insurance.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ClaimResponse(
        String id,
        String userId,
        String claimNumber,
        String policyId,
        double repairCost,
        double deductible,
        double payoutAmount,
        String description,
        String status,
        String claimDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)implements Serializable {}
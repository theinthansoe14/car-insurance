package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.Policy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomPolicyRepository {

    Policy updatePolicyDetails(String id, String policyNumber, String userId, String vehicleId, LocalDate startDate, LocalDate endDate, boolean hasClaim, LocalDateTime updatedAt);
}

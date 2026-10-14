package com.tts.car_insurance.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String name,
        String email,
        String role,
        LocalDate dateOfBirth,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)implements Serializable {}
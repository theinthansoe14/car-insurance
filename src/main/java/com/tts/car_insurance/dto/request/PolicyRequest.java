package com.tts.car_insurance.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PolicyRequest(
        @NotBlank(message = "Policy number cannot be blank")
        String policyNumber,

        @NotBlank(message = "User ID cannot be blank")
        String userId,

        @NotBlank(message = "Vehicle ID cannot be blank")
        String vehicleId,

        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be today or in the future")
        LocalDate startDate,

        @NotNull(message = "End date cannot be null")
        @Future(message = "End date must be in the future")
        LocalDate endDate,

        boolean hasClaim
) {}
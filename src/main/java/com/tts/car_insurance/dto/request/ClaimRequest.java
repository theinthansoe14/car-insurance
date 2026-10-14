package com.tts.car_insurance.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClaimRequest(
        @NotBlank(message = "Claim number cannot be blank")
        String claimNumber,

        @NotBlank(message = "Policy ID cannot be blank")
        String policyId,

        @NotBlank(message = "UserId ID cannot be blank")
        String userId,


        @PositiveOrZero(message = "Repair cost must be zero or a positive value")
        double repairCost,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Claim date cannot be null")
        @PastOrPresent(message = "Claim date must be today or in the past")
        LocalDate claimDate
) {}
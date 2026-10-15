package com.tts.car_insurance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Policy Request Payload")
public record PolicyRequest(

        @Schema(description = "Unique policy reference number", example = "POL-2026-001")
        @NotBlank(message = "Policy number cannot be blank")
        String policyNumber,

        @Schema(description = "ID of the user who owns the policy", example = "65a4b3c21f00000000000001")
        @NotBlank(message = "User ID cannot be blank")
        String userId,

        @Schema(description = "ID of the insured vehicle", example = "65a4b3c21f00000000000002")
        @NotBlank(message = "Vehicle ID cannot be blank")
        String vehicleId,

        @Schema(description = "Policy coverage start date", example = "2026-10-15")
        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be today or in the future")
        LocalDate startDate,

        @Schema(description = "Policy coverage end date", example = "2027-10-15")
        @NotNull(message = "End date cannot be null")
        @Future(message = "End date must be in the future")
        LocalDate endDate,

        @Schema(description = "Indicates whether the policy has an active claim", example = "false")
        boolean hasClaim
) {}
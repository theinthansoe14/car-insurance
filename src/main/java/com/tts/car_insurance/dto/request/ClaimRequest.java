package com.tts.car_insurance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Claim Request Payload")
public record ClaimRequest(

        @Schema(description = "Unique claim reference number", example = "CLM-987654")
        @NotBlank(message = "Claim number cannot be blank")
        String claimNumber,

        @Schema(description = "Associated policy ID", example = "65a4b3c21f00000000000001")
        @NotBlank(message = "Policy ID cannot be blank")
        String policyId,

        @Schema(description = "ID of the user filing the claim", example = "65a4b3c21f00000000000002")
        @NotBlank(message = "UserId ID cannot be blank")
        String userId,


        @Schema(description = "Estimated or actual repair cost", example = "1500.50")
        @PositiveOrZero(message = "Repair cost must be zero or a positive value")
        double repairCost,

        @Schema(description = "Detailed description of the claim incident", example = "Minor bumper scratch due to parking collision")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @Schema(description = "Date when the claim occurred", example = "2026-10-15")
        @NotNull(message = "Claim date cannot be null")
        @PastOrPresent(message = "Claim date must be today or in the past")
        LocalDate claimDate
) {}
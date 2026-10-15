package com.tts.car_insurance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Policy Response DTO")
public record PolicyResponse(

        @Schema(description = "Unique identifier of the policy", example = "65a4b3c21f00000000000001")
        String id,

        @Schema(description = "Policy reference number", example = "POL-2026-001")
        String policyNumber,

        @Schema(description = "ID of the user who owns the policy", example = "65a4b3c21f00000000000002")
        String userId,

        @Schema(description = "ID of the insured vehicle", example = "65a4b3c21f00000000000003")
        String vehicleId,

        @Schema(description = "Policy coverage start date", example = "2026-10-15")
        LocalDate startDate,

        @Schema(description = "Policy coverage end date", example = "2027-10-15")
        LocalDate endDate,

        @Schema(description = "Calculated premium amount for the policy", example = "1200.00")
        double premiumAmount,

        @Schema(description = "Current status of the policy", example = "ACTIVE")
        String status,

        @Schema(description = "Indicates whether the policy has an active claim", example = "false")
        boolean hasClaim,

        @Schema(description = "Timestamp when the record was created", example = "2026-10-15T06:47:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the record was last updated", example = "2026-10-15T06:47:00")
        LocalDateTime updatedAt
) implements Serializable {}
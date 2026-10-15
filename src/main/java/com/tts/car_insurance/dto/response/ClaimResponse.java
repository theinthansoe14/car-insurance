package com.tts.car_insurance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Claim Response DTO")
public record ClaimResponse(

        @Schema(description = "Unique identifier of the claim", example = "65a4b3c21f00000000000001")
        String id,

        @Schema(description = "ID of the user who filed the claim", example = "65a4b3c21f00000000000002")
        String userId,

        @Schema(description = "Claim reference number", example = "CLM-987654")
        String claimNumber,

        @Schema(description = "Associated policy ID", example = "65a4b3c21f00000000000003")
        String policyId,

        @Schema(description = "Total repair cost", example = "1500.50")
        double repairCost,

        @Schema(description = "Deductible amount applied", example = "250.00")
        double deductible,

        @Schema(description = "Final payout amount provided", example = "1250.50")
        double payoutAmount,

        @Schema(description = "Detailed description of the claim", example = "Minor bumper scratch due to parking collision")
        String description,

        @Schema(description = "Current status of the claim", example = "PENDING")
        String status,

        @Schema(description = "Date when the claim occurred", example = "2026-10-15")
        String claimDate,

        @Schema(description = "Timestamp when the record was created", example = "2026-10-15T06:45:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the record was last updated", example = "2026-10-15T06:45:00")
        LocalDateTime updatedAt
) implements Serializable {}
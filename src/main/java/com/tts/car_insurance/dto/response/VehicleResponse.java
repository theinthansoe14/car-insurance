package com.tts.car_insurance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Vehicle Response DTO")
public record VehicleResponse(

        @Schema(description = "Unique identifier of the vehicle", example = "65a4b3c21f00000000000001")
        String id,

        @Schema(description = "ID of the user who owns the vehicle", example = "65a4b3c21f00000000000002")
        String userId,

        @Schema(description = "Vehicle license plate number", example = "AB-1234")
        String licensePlate,

        @Schema(description = "Vehicle brand or manufacturer", example = "Toyota")
        String brand,

        @Schema(description = "Vehicle model", example = "Camry")
        String model,

        @Schema(description = "Vehicle manufacturing year", example = "2023")
        int year,

        @Schema(description = "Estimated market value of the car", example = "25000.00")
        double carValue,

        @Schema(description = "Timestamp when the record was created", example = "2026-10-15T06:47:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the record was last updated", example = "2026-10-15T06:47:00")
        LocalDateTime updatedAt
) implements Serializable {}
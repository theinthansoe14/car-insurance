package com.tts.car_insurance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "User Response DTO")
public record UserResponse(

        @Schema(description = "Unique identifier of the user", example = "65a4b3c21f00000000000001")
        String id,

        @Schema(description = "Full name of the user", example = "John Doe")
        String name,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Assigned user role", example = "USER")
        String role,

        @Schema(description = "User's date of birth", example = "1995-05-20")
        LocalDate dateOfBirth,

        @Schema(description = "Timestamp when the record was created", example = "2026-10-15T06:47:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the record was last updated", example = "2026-10-15T06:47:00")
        LocalDateTime updatedAt
) implements Serializable {}
package com.tts.car_insurance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login Response Payload containing user email and authentication token")
public record LoginResponse(

        @Schema(description = "Authenticated user's email address", example = "user@example.com")
        String email,

        @Schema(description = "JWT authentication token for subsequent requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {}
package com.tts.car_insurance.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Login Request Payload")
public record LoginRequest(

        @Schema(description = "User registered email address", example = "user@example.com")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User password", example = "password123")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 3, message = "Password must be at least 3 characters long")
        String password
) {
}

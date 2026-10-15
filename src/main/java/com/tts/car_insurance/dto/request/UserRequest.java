package com.tts.car_insurance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "User Registration and Update Request Payload")
public record UserRequest(

        @Schema(description = "Full name of the user", example = "John Doe")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User's account password (minimum 6 characters)", example = "securePassword123")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @Schema(description = "Assigned user role", example = "USER")
        @NotBlank(message = "Role cannot be blank")
        String role,

        @Schema(description = "User's date of birth (must be in the past)", example = "1995-05-20")
        @NotNull(message = "Date Of Birth cannot be blank")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth

) {}
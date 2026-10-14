package com.tts.car_insurance.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotBlank(message = "Role cannot be blank")
        String role,

        @NotNull(message = "Date Of Birth cannot be blank")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth

) {}
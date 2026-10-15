package com.tts.car_insurance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Vehicle Request Payload")
public record VehicleRequest(

        @Schema(description = "Vehicle license plate number", example = "AB-1234")
        @NotBlank(message = "License plate cannot be blank")
        String licensePlate,

        @Schema(description = "Vehicle brand or manufacturer", example = "Toyota")
        @NotBlank(message = "Brand cannot be blank")
        String brand,

        @Schema(description = "Vehicle model", example = "Camry")
        @NotBlank(message = "Model cannot be blank")
        String model,

        @Schema(description = "Vehicle manufacturing year", example = "2023")
        @Min(value = 1886, message = "Year must be a valid manufacturing year")
        @Max(value = 2027, message = "Year cannot be in the distant future")
        int year,

        @Schema(description = "Estimated market value of the car", example = "25000.00")
        @Positive(message = "Car value must be greater than zero")
        double carValue
) {}
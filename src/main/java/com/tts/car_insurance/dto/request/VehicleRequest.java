package com.tts.car_insurance.dto.request;

import jakarta.validation.constraints.*;

public record VehicleRequest(

        @NotBlank(message = "License plate cannot be blank")
        String licensePlate,

        @NotBlank(message = "Brand cannot be blank")
        String brand,

        @NotBlank(message = "Model cannot be blank")
        String model,

        @Min(value = 1886, message = "Year must be a valid manufacturing year")
        @Max(value = 2027, message = "Year cannot be in the distant future")
        int year,

        @Positive(message = "Car value must be greater than zero")
        double carValue
) {}
package com.tts.car_insurance.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;
    private String userId;
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private double carValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
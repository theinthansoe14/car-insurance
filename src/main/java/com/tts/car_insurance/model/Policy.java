package com.tts.car_insurance.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "policies")
public class Policy {
    @Id
    private String id;
    private String policyNumber;
    private String userId;
    private String vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double premiumAmount;
    private String status;
    private boolean hasClaim;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
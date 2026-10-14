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
@Document(collection = "claims")
public class Claim {
    @Id
    private String id;
    private String userId;
    private String claimNumber;
    private String policyId;
    private double repairCost;
    private double deductible;
    private double payoutAmount;
    private String description;
    private String status;
    private LocalDate claimDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
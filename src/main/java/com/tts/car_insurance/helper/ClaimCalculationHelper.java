package com.tts.car_insurance.helper;

import org.springframework.stereotype.Component;

@Component
public class ClaimCalculationHelper {

    private static final double DEDUCTIBLE_RATE = 0.10;

    public double calculateDeductible(double repairCost) {
        return repairCost * DEDUCTIBLE_RATE;
    }

    public double calculatePayoutAmount(double repairCost, double deductible) {
        return repairCost - deductible;
    }
}

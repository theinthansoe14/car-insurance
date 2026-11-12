package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.EligibilityException;

public class ExceedsVehicleValueException extends EligibilityException {

    public ExceedsVehicleValueException(String message) {
        super(message);
    }
}

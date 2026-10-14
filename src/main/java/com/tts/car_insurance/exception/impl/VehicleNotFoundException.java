package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.NotFoundException;

public class VehicleNotFoundException extends NotFoundException {

    public VehicleNotFoundException(String message) {
        super(message);
    }
}

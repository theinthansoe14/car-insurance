package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.NotFoundException;

public class ClaimNotFoundException extends NotFoundException {

    public ClaimNotFoundException(String message) {
        super(message);
    }
}

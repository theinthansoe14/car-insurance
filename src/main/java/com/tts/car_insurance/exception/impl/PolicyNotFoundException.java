package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.NotFoundException;

public class PolicyNotFoundException extends NotFoundException {

    public PolicyNotFoundException(String message) {
        super(message);
    }
}

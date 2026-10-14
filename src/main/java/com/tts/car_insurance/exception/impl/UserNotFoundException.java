package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

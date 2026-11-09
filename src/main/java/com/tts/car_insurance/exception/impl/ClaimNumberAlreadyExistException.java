package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.AlreadyExistException;

public class ClaimNumberAlreadyExistException extends AlreadyExistException {

    public ClaimNumberAlreadyExistException(String message) {
        super(message);
    }
}

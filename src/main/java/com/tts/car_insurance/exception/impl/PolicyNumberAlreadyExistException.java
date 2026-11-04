package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.AlreadyExistException;

public class PolicyNumberAlreadyExistException extends AlreadyExistException {

    public PolicyNumberAlreadyExistException(String message) {
        super(message);
    }
}

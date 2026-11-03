package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.AlreadyExistException;

public class LicensePlateAlreadyExistException extends AlreadyExistException {

    public LicensePlateAlreadyExistException(String message) {
        super(message);
    }
}

package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.EligibilityException;

public class UnderageException extends EligibilityException {

    public UnderageException(String message) {
        super(message);
    }
}

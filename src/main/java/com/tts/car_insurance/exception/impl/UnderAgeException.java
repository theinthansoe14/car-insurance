package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.EligibilityException;

public class UnderAgeException extends EligibilityException {

    public UnderAgeException(String message) {
        super(message);
    }
}

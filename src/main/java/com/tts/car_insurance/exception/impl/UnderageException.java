package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.BusinessException;

public class UnderageException extends BusinessException {

    public UnderageException(String message) {
        super(message);
    }
}

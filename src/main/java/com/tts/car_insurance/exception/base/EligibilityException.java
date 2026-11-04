package com.tts.car_insurance.exception.base;

public abstract class EligibilityException extends RuntimeException {

    protected EligibilityException(String message){
        super(message);
    };
}

package com.tts.car_insurance.exception.base;

public abstract class BusinessException extends RuntimeException {

    protected BusinessException(String message){
        super(message);
    }
}

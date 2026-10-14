package com.tts.car_insurance.exception.base;

public abstract class NotFoundException extends RuntimeException{

    protected NotFoundException(String message) {

        super(message);
    }
}

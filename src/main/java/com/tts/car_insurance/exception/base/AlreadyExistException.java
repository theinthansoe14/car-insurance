package com.tts.car_insurance.exception.base;

public abstract class AlreadyExistException extends RuntimeException{

    protected AlreadyExistException(String message){
        super(message);
    }
}

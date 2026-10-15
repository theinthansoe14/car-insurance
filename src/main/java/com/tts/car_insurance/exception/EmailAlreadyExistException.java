package com.tts.car_insurance.exception;

public class EmailAlreadyExistException extends RuntimeException{


    public EmailAlreadyExistException(String message){
        super(message);
    }
}

package com.tts.car_insurance.exception.impl;

import com.tts.car_insurance.exception.base.AlreadyExistException;

public class EmailAlreadyExistException extends AlreadyExistException {


    public EmailAlreadyExistException(String message){
        super(message);
    }
}

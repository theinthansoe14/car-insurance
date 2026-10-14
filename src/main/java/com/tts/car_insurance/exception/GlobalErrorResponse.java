package com.tts.car_insurance.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class GlobalErrorResponse {

    private int errorCode;
    private HttpStatus status;
    private String  message;
    private String path;

}

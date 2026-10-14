package com.tts.car_insurance.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ROLE {

    ADMIN ("admin"),
    USER("user");


    @JsonValue
    private final String role;

    ROLE(String role){

        this.role=role;
    }

    public static ROLE resolve(String role) {
        if(role==null)throw new IllegalArgumentException("Role cannot be empty");
        return switch (role.toLowerCase()){
            case "admin" -> ADMIN;
            case "user"  -> USER;
            default -> throw new IllegalArgumentException("Role cannot be empty");
        };

    }
}

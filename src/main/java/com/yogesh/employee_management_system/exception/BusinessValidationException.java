package com.yogesh.employee_management_system.exception;

public class BusinessValidationException extends RuntimeException{

    public BusinessValidationException(String message) {
        super(message);
    }
}

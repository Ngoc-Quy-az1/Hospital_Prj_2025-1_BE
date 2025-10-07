package com.example.Hospital.exception;

/**
 * Exception cho module quản lý bệnh nhân
 */
public class PatientException extends AppException {
    
    public PatientException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public PatientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public PatientException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}




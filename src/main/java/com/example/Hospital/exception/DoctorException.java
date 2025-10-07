package com.example.Hospital.exception;

/**
 * Exception cho module quản lý bác sĩ
 */
public class DoctorException extends AppException {
    
    public DoctorException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public DoctorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public DoctorException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}






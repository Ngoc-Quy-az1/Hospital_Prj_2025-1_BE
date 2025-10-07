package com.example.Hospital.exception;

/**
 * Exception cho module đặt lịch khám
 */
public class AppointmentException extends AppException {
    
    public AppointmentException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public AppointmentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public AppointmentException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}






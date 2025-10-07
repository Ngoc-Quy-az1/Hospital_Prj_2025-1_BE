package com.example.Hospital.exception;

/**
 * Exception cho module phẫu thuật
 */
public class SurgeryException extends AppException {
    
    public SurgeryException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public SurgeryException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public SurgeryException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}






package com.example.Hospital.exception;

/**
 * Exception cho module quản lý thuốc
 */
public class MedicineException extends AppException {
    
    public MedicineException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public MedicineException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public MedicineException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}






package com.example.Hospital.exception;

/**
 * Exception cho module thanh toán
 */
public class PaymentException extends AppException {
    
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public PaymentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public PaymentException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}




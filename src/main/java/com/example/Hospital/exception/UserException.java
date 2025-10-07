package com.example.Hospital.exception;

/**
 * Exception cho module quản lý người dùng
 */
public class UserException extends AppException {
    
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public UserException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}




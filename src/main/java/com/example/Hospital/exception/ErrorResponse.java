package com.example.Hospital.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class response cho error
 */
public class ErrorResponse {
    
    /**
     * Mã lỗi
     */
    private int code;
    
    /**
     * Thông báo lỗi
     */
    private String message;
    
    /**
     * Chi tiết lỗi (dành cho validation errors)
     */
    private Map<String, String> details;
    
    /**
     * Thời gian xảy ra lỗi
     */
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Đường dẫn API gây ra lỗi
     */
    private String path;
    
    // Constructors
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(int code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    // Builder pattern
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
    
    public static class ErrorResponseBuilder {
        private int code;
        private String message;
        private Map<String, String> details;
        private LocalDateTime timestamp = LocalDateTime.now();
        private String path;
        
        public ErrorResponseBuilder code(int code) {
            this.code = code;
            return this;
        }
        
        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ErrorResponseBuilder details(Map<String, String> details) {
            this.details = details;
            return this;
        }
        
        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }
        
        public ErrorResponse build() {
            ErrorResponse response = new ErrorResponse();
            response.code = this.code;
            response.message = this.message;
            response.details = this.details;
            response.timestamp = this.timestamp;
            response.path = this.path;
            return response;
        }
    }
    
    // Getters and Setters
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, String> getDetails() {
        return details;
    }
    
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}

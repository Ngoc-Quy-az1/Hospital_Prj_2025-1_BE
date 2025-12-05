package com.example.Hospital.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler cho toàn bộ hệ thống bệnh viện
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Xử lý AppException - exception tùy chỉnh của hệ thống
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        log.error("AppException: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .build();
                
        return ResponseEntity.status(ex.getErrorCode().getStatusCode())
                .body(errorResponse);
    }
    
    /**
     * Xử lý validation exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("ValidationException: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.INVALID_DATA.getCode())
                .message("Dữ liệu không hợp lệ")
                .details(errors)
                .build();
                
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * Xử lý RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message("Có lỗi xảy ra trong hệ thống")
                .build();
                
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    
    /**
     * Xử lý Spring Security AuthenticationException
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("AuthenticationException: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.UNAUTHENTICATED.getCode())
                .message("Chưa xác thực hoặc token không hợp lệ")
                .build();
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
    
    /**
     * Xử lý Spring Security AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("AccessDeniedException: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.UNAUTHORIZED.getCode())
                .message("Bạn không có quyền truy cập tài nguyên này")
                .build();
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }
    
    /**
     * Xử lý tất cả exception khác
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message("Có lỗi không xác định xảy ra")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}

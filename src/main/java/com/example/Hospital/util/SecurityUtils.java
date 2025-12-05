package com.example.Hospital.util;

import com.example.Hospital.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class để lấy thông tin user từ SecurityContext
 */
public class SecurityUtils {
    
    /**
     * Lấy Users object từ SecurityContext
     */
    public static Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Users) {
            return (Users) authentication.getDetails();
        }
        return null;
    }
    
    /**
     * Lấy username từ SecurityContext
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * Kiểm tra xem user đã authenticated chưa
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}


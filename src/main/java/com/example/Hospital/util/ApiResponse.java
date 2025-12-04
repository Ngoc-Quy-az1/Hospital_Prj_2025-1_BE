package com.example.Hospital.util;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    
    public static Map<String, Object> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
    
    public static Map<String, Object> success(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
    
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
    
    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    public static Map<String, Object> list(Object[] items) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", items);
        response.put("totalElements", items.length);
        response.put("totalPages", 1);
        response.put("size", items.length);
        response.put("number", 0);
        return response;
    }
    
    public static java.util.List<Object> emptyList() {
        return new java.util.ArrayList<>();
    }
}


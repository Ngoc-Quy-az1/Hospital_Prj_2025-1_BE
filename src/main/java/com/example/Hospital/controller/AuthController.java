package com.example.Hospital.controller;

import com.example.Hospital.entity.OtpCode;
import com.example.Hospital.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller cho các API authentication mà FE đang sử dụng.
 * Không dùng ApiResponse để bọc thêm, trả đúng format FE mong đợi.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Đăng ký tài khoản mới.
     * FE gửi: { username, email, password, role, roleId, phoneNumber }
     * FE chấp nhận:
     * - String chứa "successfully"
     * - Hoặc object có success=true, message=...
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> body) {
        return authService.register(body);
    }

    /**
     * Xác thực OTP cho đăng ký.
     * Endpoint FE: POST /api/auth/verify-register-otp với { email, otp }
     * Trả: { success: true/false, message: '...' }
     */
    @PostMapping("/verify-register-otp")
    public Map<String, Object> verifyRegisterOtp(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String otp = (String) body.get("otp");
        boolean ok = authService.verifyRegisterOtp(email, otp);
        return ok
                ? Map.of("success", true, "message", "Account activated successfully")
                : Map.of("success", false, "message", "OTP không hợp lệ hoặc đã hết hạn");
    }

    /**
     * Gửi lại OTP cho đăng ký.
     * Endpoint FE: POST /api/auth/resend-register-otp với { email }
     * FE mong chuỗi "New OTP sent to your email" hoặc tương đương.
     */
    @PostMapping("/resend-register-otp")
    public String resendRegisterOtp(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        authService.resendRegisterOtp(email);
        return "New OTP sent to your email";
    }

    /**
     * Đăng nhập.
     * FE gửi: { identifier, password }
     * Trả: { accessToken, refreshToken, user: { id, name, email, role } }
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) {
        String identifier = (String) body.get("identifier");
        String password = (String) body.get("password");
        return authService.login(identifier, password);
    }

    /**
     * Refresh token.
     * FE gửi: { refreshToken }
     * Trả: { accessToken, refreshToken }
     */
    @PostMapping("/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, Object> body) {
        String refreshToken = (String) body.get("refreshToken");
        return authService.refresh(refreshToken);
    }

    /**
     * Đăng xuất.
     * FE gọi: POST /api/auth/logout, không gửi gì, chỉ có header Authorization.
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        authService.logout(accessToken);
        return Map.of("success", true, "message", "Logout successfully");
    }

    /**
     * Quên mật khẩu: sinh OTP gửi về email.
     * FE gọi: POST /api/auth/forgot-password với { email }
     * Trả: { success: true, message: "New OTP sent to your email" }
     */
    @PostMapping("/forgot-password")
    public Map<String, Object> forgotPassword(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        authService.forgotPassword(email);
        return Map.of("success", true, "message", "New OTP sent to your email");
    }

    /**
     * Reset mật khẩu sau khi đã có OTP (FE sẽ tự đảm bảo gọi đúng).
     * FE gọi: POST /api/auth/reset-password với { email, otp, newPassword }
     */
    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String otp = (String) body.get("otp");
        String newPassword = (String) body.get("newPassword");
        boolean ok = authService.resetPassword(email, otp, newPassword);
        return ok
                ? Map.of("success", true, "message", "Password reset successfully")
                : Map.of("success", false, "message", "OTP không hợp lệ hoặc đã hết hạn");
    }

    /**
     * Debug OTP (dev): FE gọi GET /api/auth/debug-otp/{email}
     */
    @GetMapping("/debug-otp/{email}")
    public OtpCode debugOtp(@PathVariable String email) {
        return authService.getLatestOtpByEmail(email);
    }
}



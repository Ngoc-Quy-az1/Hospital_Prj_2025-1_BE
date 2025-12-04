package com.example.Hospital.service;

import com.example.Hospital.entity.OtpCode;
import com.example.Hospital.entity.Roles;
import com.example.Hospital.entity.UserSessions;
import com.example.Hospital.entity.Users;
import com.example.Hospital.exception.ErrorCode;
import com.example.Hospital.exception.UserException;
import com.example.Hospital.repository.OtpCodeRepository;
import com.example.Hospital.repository.RolesRepository;
import com.example.Hospital.repository.UserSessionsRepository;
import com.example.Hospital.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserSessionsRepository userSessionsRepository;

    @Autowired
    private OtpCodeRepository otpCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:}")
    private String mailFrom;

    @Value("${jwt.secret:hospital-management-system-secret-key-2025}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration:604800000}")
    private long jwtRefreshExpirationMs;

    private Key getSigningKey() {
        // Dùng trực tiếp chuỗi secret, không decode base64 để tránh lỗi ký tự không hợp lệ
        byte[] keyBytes = jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Đăng ký tài khoản mới + sinh OTP gửi email.
     * body từ FE gồm: username, email, password, role, roleId, phoneNumber.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> register(Map<String, Object> body) {
        String username = (String) body.get("username");
        String email = (String) body.get("email");
        String rawPassword = (String) body.get("password");
        Integer roleId = body.get("roleId") instanceof Number ? ((Number) body.get("roleId")).intValue() : null;

        if (username == null || username.isBlank()) {
            throw new UserException(ErrorCode.USERNAME_INVALID);
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }
        if (usersRepository.existsByUsername(username)) {
            throw new UserException(ErrorCode.USER_EXISTED);
        }
        if (email != null && usersRepository.existsByEmail(email)) {
            throw new UserException(ErrorCode.USER_EXISTED);
        }

        Roles role = null;
        if (roleId != null) {
            role = rolesRepository.findById(roleId).orElse(null);
        }
        if (role == null) {
            // fallback: cố tìm theo tên benhnhan
            role = rolesRepository.findByTenRole("benhnhan")
                    .orElseGet(() -> rolesRepository.findAll().stream().findFirst().orElse(null));
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setRole(role);
        user.setTrangThai(Users.TrangThai.active);
        usersRepository.save(user);

        // Tạo OTP cho đăng ký
        String code = generateOtpCode();
        OtpCode otp = new OtpCode();
        otp.setUsername(username);
        otp.setEmail(email);
        otp.setCode(code);
        otp.setStatus(OtpCode.Status.active);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpCodeRepository.save(otp);

        // Gửi email OTP nếu mailSender đã được cấu hình
        sendOtpEmail(email, code, "Đăng ký tài khoản");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Register successfully and OTP sent to email");
        // Dev mode: trả OTP để FE hiển thị nếu muốn
        result.put("devOTP", code);
        result.put("devMessage", "Mail sender chưa được cấu hình, hãy dùng OTP này để test.");
        return result;
    }

    /**
     * Xác thực OTP cho đăng ký bằng email + mã OTP.
     */
    public boolean verifyRegisterOtp(String email, String otpCode) {
        if (email == null || otpCode == null) return false;

        Optional<OtpCode> otpOpt = otpCodeRepository
                .findTopByUsernameAndStatusOrderByCreatedAtDesc(email, OtpCode.Status.active);

        // Nếu lưu theo username, ta thử tìm theo email trong tất cả OTP
        if (otpOpt.isEmpty()) {
            otpOpt = otpCodeRepository.findAll().stream()
                    .filter(o -> OtpCode.Status.active.equals(o.getStatus()) && email.equalsIgnoreCase(o.getEmail()))
                    .reduce((first, second) -> second);
        }

        if (otpOpt.isEmpty()) return false;

        OtpCode otp = otpOpt.get();
        if (!otp.getCode().equals(otpCode)) {
            return false;
        }
        if (otp.getExpiresAt() != null && otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpCode.Status.expired);
            otpCodeRepository.save(otp);
            return false;
        }

        // Xóa hẳn OTP sau khi xác nhận thành công
        otpCodeRepository.delete(otp);
        return true;
    }

    /**
     * Gửi lại OTP đăng ký.
     */
    public void resendRegisterOtp(String email) {
        if (email == null || email.isBlank()) return;
        // Tìm user theo email để lấy username
        Users user = usersRepository.findByEmail(email).orElse(null);
        String username = user != null ? user.getUsername() : email;

        String code = generateOtpCode();
        OtpCode otp = new OtpCode();
        otp.setUsername(username);
        otp.setEmail(email);
        otp.setCode(code);
        otp.setStatus(OtpCode.Status.active);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpCodeRepository.save(otp);
        sendOtpEmail(email, code, "Đăng ký tài khoản");
    }

    /**
     * Đăng nhập: FE gửi identifier (username/email/phone) + password.
     * Trả về: accessToken, refreshToken, user{...}
     */
    public Map<String, Object> login(String identifier, String rawPassword) {
        if (identifier == null || rawPassword == null) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }

        Users user = usersRepository.findByUsername(identifier)
                .or(() -> usersRepository.findByEmail(identifier))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UserException(ErrorCode.WRONG_PASSWORD);
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        UserSessions session = new UserSessions();
        session.setUser(user);
        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(2));
        session.setRefreshExpiresAt(LocalDateTime.now().plusDays(7));
        session.setIsRevoked(false);
        userSessionsRepository.save(session);

        Map<String, Object> userDto = new HashMap<>();
        userDto.put("id", user.getUserId());
        userDto.put("username", user.getUsername());
        userDto.put("email", user.getEmail());
        userDto.put("role", user.getRole() != null ? user.getRole().getTenRole() : null);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("user", userDto);
        return result;
    }

    /**
     * Refresh token: tạo accessToken mới từ refreshToken còn hạn.
     */
    public Map<String, Object> refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UserException(ErrorCode.INVALID_TOKEN);
        }

        UserSessions session = userSessionsRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UserException(ErrorCode.SESSION_NOT_EXISTED));

        if (Boolean.TRUE.equals(session.getIsRevoked())) {
            throw new UserException(ErrorCode.SESSION_EXPIRED);
        }
        if (session.getRefreshExpiresAt() != null &&
                session.getRefreshExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserException(ErrorCode.TOKEN_EXPIRED);
        }

        String newAccessToken = generateAccessToken(session.getUser());
        String newRefreshToken = generateRefreshToken(session.getUser());

        session.setAccessToken(newAccessToken);
        session.setRefreshToken(newRefreshToken);
        session.setExpiresAt(LocalDateTime.now().plusHours(2));
        session.setRefreshExpiresAt(LocalDateTime.now().plusDays(7));
        userSessionsRepository.save(session);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);
        return result;
    }

    /**
     * Đăng xuất: revoke theo accessToken.
     */
    public void logout(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return;
        }
        userSessionsRepository.findByAccessToken(accessToken)
                .ifPresent(s -> {
                    s.setIsRevoked(true);
                    userSessionsRepository.save(s);
                });
    }

    /**
     * Quên mật khẩu: sinh OTP gửi về email.
     */
    public void forgotPassword(String email) {
        if (email == null || email.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_EXISTED));

        String code = generateOtpCode();
        OtpCode otp = new OtpCode();
        otp.setUsername(user.getUsername());
        otp.setEmail(email);
        otp.setCode(code);
        otp.setStatus(OtpCode.Status.active);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpCodeRepository.save(otp);

        // Gửi email OTP cho quên mật khẩu
        sendOtpEmail(email, code, "Quên mật khẩu");
    }

    /**
     * Reset mật khẩu với email + OTP + mật khẩu mới.
     */
    public boolean resetPassword(String email, String otpCode, String newPassword) {
        if (email == null || otpCode == null || newPassword == null) {
            return false;
        }
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_EXISTED));

        Optional<OtpCode> otpOpt = otpCodeRepository.findAll().stream()
                .filter(o -> OtpCode.Status.active.equals(o.getStatus())
                        && email.equalsIgnoreCase(o.getEmail()))
                .reduce((first, second) -> second);

        if (otpOpt.isEmpty()) return false;

        OtpCode otp = otpOpt.get();
        if (!otp.getCode().equals(otpCode)) {
            return false;
        }
        if (otp.getExpiresAt() != null && otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpCode.Status.expired);
            otpCodeRepository.save(otp);
            return false;
        }

        // OTP đúng và còn hạn -> xóa record
        otpCodeRepository.delete(otp);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
        return true;
    }

    /**
     * Lấy OTP mới nhất theo email (debug).
     */
    public OtpCode getLatestOtpByEmail(String email) {
        return otpCodeRepository.findAll().stream()
                .filter(o -> email.equalsIgnoreCase(o.getEmail()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private String generateOtpCode() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }

    private String generateAccessToken(Users user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtExpirationMs);
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", user.getRole() != null ? user.getRole().getTenRole() : null)
                .setIssuedAt(java.util.Date.from(now))
                .setExpiration(java.util.Date.from(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Users user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtRefreshExpirationMs);
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("type", "refresh")
                .setIssuedAt(java.util.Date.from(now))
                .setExpiration(java.util.Date.from(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gửi email chứa mã OTP, nếu đã cấu hình JavaMailSender.
     * Không ném lỗi ra ngoài để tránh làm vỡ flow FE khi SMTP gặp sự cố.
     */
    private void sendOtpEmail(String toEmail, String otpCode, String subjectSuffix) {
        if (mailSender == null || toEmail == null || toEmail.isBlank()) {
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (mailFrom != null && !mailFrom.isBlank()) {
                message.setFrom(mailFrom);
            }
            message.setTo(toEmail);
            message.setSubject("[Hospital] Mã OTP " + subjectSuffix);
            String body = "Xin chào,\n\n"
                    + "Mã OTP của bạn là: " + otpCode + "\n"
                    + "Mã có hiệu lực trong 10 phút.\n\n"
                    + "Nếu bạn không yêu cầu thao tác này, vui lòng bỏ qua email.\n\n"
                    + "Trân trọng,\n"
                    + "Hospital Management System";
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ignored) {
            // Không làm hỏng luồng chính nếu gửi mail lỗi
        }
    }
}



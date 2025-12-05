package com.example.Hospital.repository;

import com.example.Hospital.entity.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho UserSessions entity
 */
@Repository
public interface UserSessionsRepository extends JpaRepository<UserSessions, Integer> {
    
    /**
     * Tìm session theo user
     */
    List<UserSessions> findByUser_UserId(Integer userId);
    
    /**
     * Tìm session theo refresh token
     */
    Optional<UserSessions> findByRefreshToken(String refreshToken);
    
    /**
     * Tìm session theo access token
     */
    Optional<UserSessions> findByAccessToken(String accessToken);
    
    /**
     * Tìm session theo access token với TRIM để xử lý khoảng trắng
     */
    @Query("SELECT us FROM UserSessions us WHERE TRIM(us.accessToken) = TRIM(:accessToken)")
    Optional<UserSessions> findByAccessTokenTrimmed(@Param("accessToken") String accessToken);
    
    /**
     * Tìm session theo IP address
     */
    List<UserSessions> findByIpAddress(String ipAddress);
    
    /**
     * Tìm session theo user agent
     */
    List<UserSessions> findByUserAgent(String userAgent);
    
    /**
     * Tìm session chưa hết hạn
     */
    @Query("SELECT us FROM UserSessions us WHERE us.expiresAt > CURRENT_TIMESTAMP AND us.isRevoked = false")
    List<UserSessions> findActiveSessions();
    
    /**
     * Tìm session đã hết hạn
     */
    @Query("SELECT us FROM UserSessions us WHERE us.expiresAt <= CURRENT_TIMESTAMP")
    List<UserSessions> findExpiredSessions();
    
    /**
     * Tìm session đã bị thu hồi
     */
    List<UserSessions> findByIsRevokedTrue();
    
    /**
     * Tìm session chưa bị thu hồi
     */
    List<UserSessions> findByIsRevokedFalse();
    
    /**
     * Tìm session theo user và trạng thái chưa hết hạn
     */
    @Query("SELECT us FROM UserSessions us WHERE us.user.userId = :userId AND us.expiresAt > CURRENT_TIMESTAMP AND us.isRevoked = false")
    List<UserSessions> findActiveSessionsByUser(@Param("userId") Integer userId);
    
    /**
     * Tìm session theo khoảng thời gian tạo
     */
    List<UserSessions> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Đếm session theo user
     */
    long countByUser_UserId(Integer userId);
    
    /**
     * Đếm session chưa hết hạn
     */
    @Query("SELECT COUNT(us) FROM UserSessions us WHERE us.expiresAt > CURRENT_TIMESTAMP AND us.isRevoked = false")
    long countActiveSessions();
    
    /**
     * Đếm session đã hết hạn
     */
    @Query("SELECT COUNT(us) FROM UserSessions us WHERE us.expiresAt <= CURRENT_TIMESTAMP")
    long countExpiredSessions();
    
    /**
     * Đếm session theo IP address
     */
    long countByIpAddress(String ipAddress);
    
    /**
     * Xóa session đã hết hạn
     */
    @Query("DELETE FROM UserSessions us WHERE us.expiresAt <= CURRENT_TIMESTAMP")
    void deleteExpiredSessions();
    
    /**
     * Xóa session đã hết hạn của user cụ thể
     */
    @Query("DELETE FROM UserSessions us WHERE us.user.userId = :userId AND us.expiresAt <= CURRENT_TIMESTAMP")
    void deleteExpiredSessionsByUser(@Param("userId") Integer userId);
}






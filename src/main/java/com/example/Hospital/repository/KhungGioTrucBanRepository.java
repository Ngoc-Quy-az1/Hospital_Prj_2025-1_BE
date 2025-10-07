package com.example.Hospital.repository;

import com.example.Hospital.entity.KhungGioTrucBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Repository interface cho KhungGioTrucBan entity
 */
@Repository
public interface KhungGioTrucBanRepository extends JpaRepository<KhungGioTrucBan, Integer> {
    
    /**
     * Tìm khung giờ trực theo thời gian
     */
    Optional<KhungGioTrucBan> findByKhunggiotruc(LocalTime khunggiotruc);
    
    /**
     * Kiểm tra khung giờ trực có tồn tại
     */
    boolean existsByKhunggiotruc(LocalTime khunggiotruc);
}






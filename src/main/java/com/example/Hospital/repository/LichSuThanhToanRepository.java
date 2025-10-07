package com.example.Hospital.repository;

import com.example.Hospital.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho LichSuThanhToan entity
 */
@Repository
public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Integer> {
    
    /**
     * Tìm lịch sử thanh toán theo thanh toán
     */
    List<LichSuThanhToan> findByThanhToan_ThanhtoanId(Integer thanhtoanId);
    
    /**
     * Tìm lịch sử thanh toán theo trạng thái mới
     */
    List<LichSuThanhToan> findByTrangThaiMoi(String trangThaiMoi);
    
    /**
     * Tìm lịch sử thanh toán theo khoảng thời gian
     */
    List<LichSuThanhToan> findByThoiGianBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm lịch sử thanh toán theo thanh toán và trạng thái mới
     */
    List<LichSuThanhToan> findByThanhToan_ThanhtoanIdAndTrangThaiMoi(Integer thanhtoanId, String trangThaiMoi);
    
    /**
     * Đếm lịch sử thanh toán theo thanh toán
     */
    long countByThanhToan_ThanhtoanId(Integer thanhtoanId);
    
    /**
     * Đếm lịch sử thanh toán theo trạng thái mới
     */
    long countByTrangThaiMoi(String trangThaiMoi);
    
    /**
     * Tìm lịch sử thanh toán gần nhất
     */
    List<LichSuThanhToan> findAllByOrderByThoiGianDesc();
}




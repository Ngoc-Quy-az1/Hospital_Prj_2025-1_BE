package com.example.Hospital.repository;

import com.example.Hospital.entity.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho LichLamViec entity
 */
@Repository
public interface LichLamViecRepository extends JpaRepository<LichLamViec, Integer> {
    
    /**
     * Tìm lịch làm việc theo bác sĩ
     */
    List<LichLamViec> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm lịch làm việc theo ngày bắt đầu
     */
    List<LichLamViec> findByNgayBatDau(LocalDate ngayBatDau);
    
    /**
     * Tìm lịch làm việc theo ngày kết thúc
     */
    List<LichLamViec> findByNgayKetThuc(LocalDate ngayKetThuc);
    
    /**
     * Tìm lịch làm việc theo ca làm
     */
    List<LichLamViec> findByCaLam(String caLam);
    
    /**
     * Tìm lịch làm việc theo khoảng thời gian
     */
    List<LichLamViec> findByNgayBatDauBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm lịch làm việc theo bác sĩ và ca làm
     */
    List<LichLamViec> findByBacsi_BacsiIdAndCaLam(Integer bacsiId, String caLam);
    
    /**
     * Tìm lịch làm việc đang diễn ra
     */
    @Query("SELECT llv FROM LichLamViec llv WHERE llv.ngayBatDau <= CURRENT_DATE AND (llv.ngayKetThuc IS NULL OR llv.ngayKetThuc >= CURRENT_DATE)")
    List<LichLamViec> findCurrentSchedules();
    
    /**
     * Tìm lịch làm việc sắp tới
     */
    @Query("SELECT llv FROM LichLamViec llv WHERE llv.ngayBatDau > CURRENT_DATE ORDER BY llv.ngayBatDau ASC")
    List<LichLamViec> findUpcomingSchedules();
    
    /**
     * Đếm lịch làm việc theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Đếm lịch làm việc theo ca làm
     */
    long countByCaLam(String caLam);
}

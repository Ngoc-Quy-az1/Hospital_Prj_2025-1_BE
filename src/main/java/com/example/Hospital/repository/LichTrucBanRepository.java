package com.example.Hospital.repository;

import com.example.Hospital.entity.LichTrucBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho LichTrucBan entity
 */
@Repository
public interface LichTrucBanRepository extends JpaRepository<LichTrucBan, Integer> {
    
    /**
     * Tìm lịch trực theo bác sĩ
     */
    List<LichTrucBan> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm lịch trực theo phòng ban
     */
    List<LichTrucBan> findByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Tìm lịch trực theo khung giờ
     */
    List<LichTrucBan> findByKhungGioTruc_KhunggiotrucId(Integer khunggiotrucId);
    
    /**
     * Tìm lịch trực theo ngày
     */
    List<LichTrucBan> findByNgayTruc(LocalDate ngayTruc);
    
    /**
     * Tìm lịch trực theo khoảng thời gian
     */
    List<LichTrucBan> findByNgayTrucBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm lịch trực theo bác sĩ và ngày
     */
    List<LichTrucBan> findByBacsi_BacsiIdAndNgayTruc(Integer bacsiId, LocalDate ngayTruc);
    
    /**
     * Tìm lịch trực theo phòng ban và ngày
     */
    List<LichTrucBan> findByPhongban_PhongbanIdAndNgayTruc(Integer phongbanId, LocalDate ngayTruc);
    
    /**
     * Đếm lịch trực theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Đếm lịch trực theo phòng ban
     */
    long countByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Tìm lịch trực hôm nay
     */
    @Query("SELECT ltb FROM LichTrucBan ltb WHERE ltb.ngayTruc = CURRENT_DATE ORDER BY ltb.khungGioTruc.khunggiotruc ASC")
    List<LichTrucBan> findTodaySchedule();
    
    /**
     * Tìm lịch trực theo bác sĩ hôm nay
     */
    @Query("SELECT ltb FROM LichTrucBan ltb WHERE ltb.bacsi.bacsiId = :bacsiId AND ltb.ngayTruc = CURRENT_DATE")
    List<LichTrucBan> findTodayScheduleByDoctor(@Param("bacsiId") Integer bacsiId);
}






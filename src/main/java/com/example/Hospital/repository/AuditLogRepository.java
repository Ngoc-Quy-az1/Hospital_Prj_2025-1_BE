package com.example.Hospital.repository;

import com.example.Hospital.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho AuditLog entity
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
    
    /**
     * Tìm audit log theo nhân viên
     */
    List<AuditLog> findByNhanvien_NhanvienId(Integer nhanvienId);
    
    /**
     * Tìm audit log theo hành động
     */
    List<AuditLog> findByHanhDong(String hanhDong);
    
    /**
     * Tìm audit log theo đối tượng
     */
    List<AuditLog> findByDoiTuong(String doiTuong);
    
    /**
     * Tìm audit log theo đối tượng ID
     */
    List<AuditLog> findByDoiTuongId(Integer doiTuongId);
    
    /**
     * Tìm audit log theo khoảng thời gian
     */
    List<AuditLog> findByThoiGianBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm audit log theo nhân viên và hành động
     */
    List<AuditLog> findByNhanvien_NhanvienIdAndHanhDong(Integer nhanvienId, String hanhDong);
    
    /**
     * Tìm audit log theo đối tượng và đối tượng ID
     */
    List<AuditLog> findByDoiTuongAndDoiTuongId(String doiTuong, Integer doiTuongId);
    
    /**
     * Đếm audit log theo nhân viên
     */
    long countByNhanvien_NhanvienId(Integer nhanvienId);
    
    /**
     * Đếm audit log theo hành động
     */
    long countByHanhDong(String hanhDong);
    
    /**
     * Đếm audit log theo đối tượng
     */
    long countByDoiTuong(String doiTuong);
    
    /**
     * Tìm audit log gần nhất
     */
    @Query("SELECT al FROM AuditLog al ORDER BY al.thoiGian DESC")
    List<AuditLog> findLatestLogs();
    
    /**
     * Tìm audit log theo nhân viên gần nhất
     */
    @Query("SELECT al FROM AuditLog al WHERE al.nhanvien.nhanvienId = :nhanvienId ORDER BY al.thoiGian DESC")
    List<AuditLog> findLatestLogsByNhanvien(@Param("nhanvienId") Integer nhanvienId);
    
    /**
     * Tìm nhân viên có nhiều hoạt động nhất
     */
    @Query("SELECT al.nhanvien FROM AuditLog al GROUP BY al.nhanvien ORDER BY COUNT(al) DESC")
    List<Object[]> findMostActiveNhanvien();
}




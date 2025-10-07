package com.example.Hospital.repository;

import com.example.Hospital.entity.BacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho BacSi entity
 */
@Repository
public interface BacSiRepository extends JpaRepository<BacSi, Integer> {
    
    /**
     * Tìm bác sĩ theo họ tên
     */
    List<BacSi> findByHoTenContainingIgnoreCase(String hoTen);
    
    /**
     * Tìm bác sĩ theo chuyên khoa
     */
    List<BacSi> findByChuyenKhoa(String chuyenKhoa);
    
    /**
     * Tìm bác sĩ theo phòng ban
     */
    List<BacSi> findByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Tìm bác sĩ theo số điện thoại
     */
    Optional<BacSi> findBySdt(String sdt);
    
    /**
     * Tìm bác sĩ theo email
     */
    Optional<BacSi> findByEmail(String email);
    
    /**
     * Tìm bác sĩ theo phòng ban và chuyên khoa
     */
    List<BacSi> findByPhongban_PhongbanIdAndChuyenKhoa(Integer phongbanId, String chuyenKhoa);
    
    /**
     * Đếm số bác sĩ theo chuyên khoa
     */
    long countByChuyenKhoa(String chuyenKhoa);
    
    /**
     * Đếm số bác sĩ theo phòng ban
     */
    long countByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Tìm tất cả chuyên khoa
     */
    @Query("SELECT DISTINCT bs.chuyenKhoa FROM BacSi bs WHERE bs.chuyenKhoa IS NOT NULL")
    List<String> findAllChuyenKhoa();
    
    /**
     * Tìm bác sĩ có nhiều lịch khám nhất
     */
    @Query("SELECT bs FROM BacSi bs ORDER BY SIZE(bs.danhSachDatLich) DESC")
    List<BacSi> findBacSiWithMostAppointments();
}

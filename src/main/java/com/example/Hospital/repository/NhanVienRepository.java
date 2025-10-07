package com.example.Hospital.repository;

import com.example.Hospital.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho NhanVien entity
 */
@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    
    /**
     * Tìm nhân viên theo họ tên
     */
    List<NhanVien> findByHoTenContainingIgnoreCase(String hoTen);
    
    /**
     * Tìm nhân viên theo chức vụ
     */
    List<NhanVien> findByChucVu(String chucVu);
    
    /**
     * Tìm nhân viên theo phòng ban
     */
    List<NhanVien> findByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Tìm nhân viên theo ngày vào làm
     */
    List<NhanVien> findByNgayVaoLam(LocalDate ngayVaoLam);
    
    /**
     * Tìm nhân viên theo khoảng thời gian vào làm
     */
    List<NhanVien> findByNgayVaoLamBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm nhân viên theo phòng ban và chức vụ
     */
    List<NhanVien> findByPhongban_PhongbanIdAndChucVu(Integer phongbanId, String chucVu);
    
    /**
     * Đếm số nhân viên theo phòng ban
     */
    long countByPhongban_PhongbanId(Integer phongbanId);
    
    /**
     * Đếm số nhân viên theo chức vụ
     */
    long countByChucVu(String chucVu);
    
    /**
     * Tìm nhân viên có lương cao nhất
     */
    @Query("SELECT nv FROM NhanVien nv WHERE nv.luong = (SELECT MAX(nv2.luong) FROM NhanVien nv2)")
    List<NhanVien> findNhanVienWithMaxLuong();
    
    /**
     * Tìm nhân viên có lương thấp nhất
     */
    @Query("SELECT nv FROM NhanVien nv WHERE nv.luong = (SELECT MIN(nv2.luong) FROM NhanVien nv2)")
    List<NhanVien> findNhanVienWithMinLuong();
}

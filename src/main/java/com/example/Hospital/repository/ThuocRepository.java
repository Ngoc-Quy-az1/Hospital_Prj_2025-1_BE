package com.example.Hospital.repository;

import com.example.Hospital.entity.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho Thuoc entity
 */
@Repository
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {
    
    /**
     * Tìm thuốc theo tên
     */
    List<Thuoc> findByTenThuocContainingIgnoreCase(String tenThuoc);
    
    /**
     * Tìm thuốc theo hoạt chất
     */
    List<Thuoc> findByHoatChatContainingIgnoreCase(String hoatChat);
    
    /**
     * Tìm thuốc theo nhà sản xuất
     */
    List<Thuoc> findByNhaSanXuatContainingIgnoreCase(String nhaSanXuat);
    
    /**
     * Tìm thuốc theo dạng bào chế
     */
    List<Thuoc> findByDangBaoChe(String dangBaoChe);
    
    /**
     * Tìm thuốc theo hàm lượng
     */
    List<Thuoc> findByHamLuong(String hamLuong);
    
    /**
     * Tìm thuốc sắp hết hạn
     */
    List<Thuoc> findByHanSuDungBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm thuốc đã hết hạn
     */
    @Query("SELECT t FROM Thuoc t WHERE t.hanSuDung < CURRENT_DATE")
    List<Thuoc> findExpiredMedicines();
    
    /**
     * Tìm thuốc sắp hết hạn trong 30 ngày
     */
    @Query("SELECT t FROM Thuoc t WHERE t.hanSuDung BETWEEN CURRENT_DATE AND :expiryDate")
    List<Thuoc> findMedicinesExpiringSoon(@Param("expiryDate") LocalDate expiryDate);
    
    /**
     * Kiểm tra thuốc có tồn tại theo tên
     */
    boolean existsByTenThuoc(String tenThuoc);
    
    /**
     * Đếm số thuốc theo nhà sản xuất
     */
    long countByNhaSanXuat(String nhaSanXuat);
    
    /**
     * Đếm số thuốc theo dạng bào chế
     */
    long countByDangBaoChe(String dangBaoChe);
    
    /**
     * Tìm thuốc được sử dụng nhiều nhất
     */
    @Query("SELECT t FROM Thuoc t ORDER BY SIZE(t.danhSachDonThuocChiTiet) DESC")
    List<Thuoc> findMostUsedMedicines();
    
    /**
     * Tìm tất cả nhà sản xuất
     */
    @Query("SELECT DISTINCT t.nhaSanXuat FROM Thuoc t WHERE t.nhaSanXuat IS NOT NULL")
    List<String> findAllManufacturers();
    
    /**
     * Tìm tất cả dạng bào chế
     */
    @Query("SELECT DISTINCT t.dangBaoChe FROM Thuoc t WHERE t.dangBaoChe IS NOT NULL")
    List<String> findAllDangBaoChe();
}

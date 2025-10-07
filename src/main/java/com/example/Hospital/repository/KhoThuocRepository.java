package com.example.Hospital.repository;

import com.example.Hospital.entity.KhoThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho KhoThuoc entity
 */
@Repository
public interface KhoThuocRepository extends JpaRepository<KhoThuoc, Integer> {
    
    /**
     * Tìm kho thuốc theo thuốc
     */
    Optional<KhoThuoc> findByThuoc_ThuocId(Integer thuocId);
    
    /**
     * Tìm kho thuốc theo vị trí
     */
    List<KhoThuoc> findByViTri(String viTri);
    
    /**
     * Tìm kho thuốc có số lượng thấp
     */
    @Query("SELECT kt FROM KhoThuoc kt WHERE kt.soLuong <= :threshold")
    List<KhoThuoc> findLowStockMedicines(@Param("threshold") Integer threshold);
    
    /**
     * Tìm kho thuốc hết hàng
     */
    @Query("SELECT kt FROM KhoThuoc kt WHERE kt.soLuong = 0")
    List<KhoThuoc> findOutOfStockMedicines();
    
    /**
     * Tìm kho thuốc có số lượng cao
     */
    @Query("SELECT kt FROM KhoThuoc kt WHERE kt.soLuong >= :threshold")
    List<KhoThuoc> findHighStockMedicines(@Param("threshold") Integer threshold);
    
    /**
     * Đếm số kho thuốc theo vị trí
     */
    long countByViTri(String viTri);
    
    /**
     * Đếm số thuốc hết hàng
     */
    @Query("SELECT COUNT(kt) FROM KhoThuoc kt WHERE kt.soLuong = 0")
    long countOutOfStockMedicines();
    
    /**
     * Tìm thuốc có số lượng tồn kho cao nhất
     */
    @Query("SELECT kt FROM KhoThuoc kt ORDER BY kt.soLuong DESC")
    List<KhoThuoc> findMedicinesWithHighestStock();
    
    /**
     * Tìm thuốc có số lượng tồn kho thấp nhất
     */
    @Query("SELECT kt FROM KhoThuoc kt WHERE kt.soLuong > 0 ORDER BY kt.soLuong ASC")
    List<KhoThuoc> findMedicinesWithLowestStock();
    
    /**
     * Tính tổng số lượng thuốc trong kho
     */
    @Query("SELECT SUM(kt.soLuong) FROM KhoThuoc kt")
    Long getTotalStockQuantity();
    
    /**
     * Tính tổng giá trị thuốc trong kho (cần thêm giá vào entity Thuoc)
     */
    @Query("SELECT COUNT(DISTINCT kt.thuoc.thuocId) FROM KhoThuoc kt")
    Long getTotalMedicineTypes();
}






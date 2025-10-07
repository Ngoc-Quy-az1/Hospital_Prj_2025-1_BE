package com.example.Hospital.repository;

import com.example.Hospital.entity.DonHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface cho DonHangChiTiet entity
 */
@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {
    
    /**
     * Tìm chi tiết đơn hàng theo đơn hàng
     */
    List<DonHangChiTiet> findByDonHangThuoc_DonhangId(Integer donhangId);
    
    /**
     * Tìm chi tiết đơn hàng theo thuốc
     */
    List<DonHangChiTiet> findByThuoc_ThuocId(Integer thuocId);
    
    /**
     * Tìm chi tiết đơn hàng theo đơn hàng và thuốc
     */
    DonHangChiTiet findByDonHangThuoc_DonhangIdAndThuoc_ThuocId(Integer donhangId, Integer thuocId);
    
    /**
     * Đếm chi tiết đơn hàng theo đơn hàng
     */
    long countByDonHangThuoc_DonhangId(Integer donhangId);
    
    /**
     * Đếm chi tiết đơn hàng theo thuốc
     */
    long countByThuoc_ThuocId(Integer thuocId);
}




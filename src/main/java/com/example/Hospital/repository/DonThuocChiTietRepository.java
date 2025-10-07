package com.example.Hospital.repository;

import com.example.Hospital.entity.DonThuocChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface cho DonThuocChiTiet entity
 */
@Repository
public interface DonThuocChiTietRepository extends JpaRepository<DonThuocChiTiet, Integer> {
    
    /**
     * Tìm chi tiết đơn thuốc theo đơn thuốc
     */
    List<DonThuocChiTiet> findByDonThuoc_DonthuocId(Integer donthuocId);
    
    /**
     * Tìm chi tiết đơn thuốc theo thuốc
     */
    List<DonThuocChiTiet> findByThuoc_ThuocId(Integer thuocId);
    
    /**
     * Tìm chi tiết đơn thuốc theo số lượng
     */
    List<DonThuocChiTiet> findBySoLuong(Integer soLuong);
    
    /**
     * Tìm chi tiết đơn thuốc theo đơn thuốc và thuốc
     */
    DonThuocChiTiet findByDonThuoc_DonthuocIdAndThuoc_ThuocId(Integer donthuocId, Integer thuocId);
    
    /**
     * Đếm chi tiết đơn thuốc theo đơn thuốc
     */
    long countByDonThuoc_DonthuocId(Integer donthuocId);
    
    /**
     * Đếm chi tiết đơn thuốc theo thuốc
     */
    long countByThuoc_ThuocId(Integer thuocId);
}




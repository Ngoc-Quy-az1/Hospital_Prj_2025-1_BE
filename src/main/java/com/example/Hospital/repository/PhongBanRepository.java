package com.example.Hospital.repository;

import com.example.Hospital.entity.PhongBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho PhongBan entity
 */
@Repository
public interface PhongBanRepository extends JpaRepository<PhongBan, Integer> {
    
    /**
     * Tìm phòng ban theo tên
     */
    Optional<PhongBan> findByTenPhongban(String tenPhongban);
    
    /**
     * Kiểm tra phòng ban có tồn tại theo tên
     */
    boolean existsByTenPhongban(String tenPhongban);
    
    /**
     * Tìm phòng ban theo tên chứa từ khóa
     */
    List<PhongBan> findByTenPhongbanContainingIgnoreCase(String tenPhongban);
    
    /**
     * Đếm số lượng nhân viên trong phòng ban
     */
    @Query("SELECT COUNT(nv) FROM NhanVien nv WHERE nv.phongban.phongbanId = :phongbanId")
    long countNhanVienByPhongbanId(@Param("phongbanId") Integer phongbanId);
    
    /**
     * Đếm số lượng bác sĩ trong phòng ban
     */
    @Query("SELECT COUNT(bs) FROM BacSi bs WHERE bs.phongban.phongbanId = :phongbanId")
    long countBacSiByPhongbanId(@Param("phongbanId") Integer phongbanId);
}




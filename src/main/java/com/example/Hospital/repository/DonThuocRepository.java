package com.example.Hospital.repository;

import com.example.Hospital.entity.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho DonThuoc entity
 */
@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, Integer> {
    
    /**
     * Tìm đơn thuốc theo bệnh nhân
     */
    List<DonThuoc> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm đơn thuốc theo bác sĩ
     */
    List<DonThuoc> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm đơn thuốc theo ngày kê
     */
    List<DonThuoc> findByNgayKe(LocalDate ngayKe);
    
    /**
     * Tìm đơn thuốc theo khoảng thời gian
     */
    List<DonThuoc> findByNgayKeBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm đơn thuốc theo bệnh nhân và bác sĩ
     */
    List<DonThuoc> findByBenhnhan_BenhnhanIdAndBacsi_BacsiId(Integer benhnhanId, Integer bacsiId);
    
    /**
     * Tìm đơn thuốc theo bệnh nhân và ngày kê
     */
    List<DonThuoc> findByBenhnhan_BenhnhanIdAndNgayKe(Integer benhnhanId, LocalDate ngayKe);
    
    /**
     * Tìm đơn thuốc theo bác sĩ và ngày kê
     */
    List<DonThuoc> findByBacsi_BacsiIdAndNgayKe(Integer bacsiId, LocalDate ngayKe);
    
    /**
     * Đếm đơn thuốc theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Đếm đơn thuốc theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Đếm đơn thuốc theo ngày
     */
    long countByNgayKe(LocalDate ngayKe);
    
    /**
     * Tìm đơn thuốc gần nhất của bệnh nhân
     */
    @Query("SELECT dt FROM DonThuoc dt WHERE dt.benhnhan.benhnhanId = :benhnhanId ORDER BY dt.ngayKe DESC")
    List<DonThuoc> findLatestByPatient(@Param("benhnhanId") Integer benhnhanId);
    
    /**
     * Tìm đơn thuốc hôm nay
     */
    @Query("SELECT dt FROM DonThuoc dt WHERE dt.ngayKe = CURRENT_DATE ORDER BY dt.ngayKe DESC")
    List<DonThuoc> findTodayPrescriptions();
    
    /**
     * Tìm bác sĩ kê nhiều đơn thuốc nhất
     */
    @Query("SELECT dt.bacsi FROM DonThuoc dt GROUP BY dt.bacsi ORDER BY COUNT(dt) DESC")
    List<Object[]> findDoctorsWithMostPrescriptions();
}






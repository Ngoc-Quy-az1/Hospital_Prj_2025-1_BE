package com.example.Hospital.repository;

import com.example.Hospital.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho LabTest entity
 */
@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Integer> {
    
    /**
     * Tìm xét nghiệm theo bệnh nhân
     */
    List<LabTest> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm xét nghiệm theo bác sĩ
     */
    List<LabTest> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm xét nghiệm theo ngày test
     */
    List<LabTest> findByNgayTest(LocalDate ngayTest);
    
    /**
     * Tìm xét nghiệm theo khoảng thời gian
     */
    List<LabTest> findByNgayTestBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm xét nghiệm theo loại xét nghiệm
     */
    List<LabTest> findByLoaiXetNghiem(String loaiXetNghiem);
    
    /**
     * Tìm xét nghiệm theo bệnh nhân và loại xét nghiệm
     */
    List<LabTest> findByBenhnhan_BenhnhanIdAndLoaiXetNghiem(Integer benhnhanId, String loaiXetNghiem);
    
    /**
     * Tìm xét nghiệm theo bác sĩ và loại xét nghiệm
     */
    List<LabTest> findByBacsi_BacsiIdAndLoaiXetNghiem(Integer bacsiId, String loaiXetNghiem);
    
    /**
     * Tìm xét nghiệm theo bệnh nhân và ngày test
     */
    List<LabTest> findByBenhnhan_BenhnhanIdAndNgayTest(Integer benhnhanId, LocalDate ngayTest);
    
    /**
     * Đếm xét nghiệm theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Đếm xét nghiệm theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Đếm xét nghiệm theo loại
     */
    long countByLoaiXetNghiem(String loaiXetNghiem);
    
    /**
     * Đếm xét nghiệm theo ngày
     */
    long countByNgayTest(LocalDate ngayTest);
    
    /**
     * Tìm xét nghiệm gần nhất của bệnh nhân
     */
    @Query("SELECT lt FROM LabTest lt WHERE lt.benhnhan.benhnhanId = :benhnhanId ORDER BY lt.ngayTest DESC")
    List<LabTest> findLatestByPatient(@Param("benhnhanId") Integer benhnhanId);
    
    /**
     * Tìm xét nghiệm hôm nay
     */
    @Query("SELECT lt FROM LabTest lt WHERE lt.ngayTest = CURRENT_DATE ORDER BY lt.ngayTest DESC")
    List<LabTest> findTodayTests();
    
    /**
     * Tìm tất cả loại xét nghiệm
     */
    @Query("SELECT DISTINCT lt.loaiXetNghiem FROM LabTest lt WHERE lt.loaiXetNghiem IS NOT NULL")
    List<String> findAllTestTypes();
    
    /**
     * Tìm xét nghiệm theo kết quả chứa từ khóa
     */
    @Query("SELECT lt FROM LabTest lt WHERE lt.ketQua LIKE %:keyword%")
    List<LabTest> findByKetQuaContaining(@Param("keyword") String keyword);
}






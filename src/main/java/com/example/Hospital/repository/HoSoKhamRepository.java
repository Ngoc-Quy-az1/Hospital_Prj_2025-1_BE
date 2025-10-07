package com.example.Hospital.repository;

import com.example.Hospital.entity.HoSoKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho HoSoKham entity
 */
@Repository
public interface HoSoKhamRepository extends JpaRepository<HoSoKham, Integer> {
    
    /**
     * Tìm hồ sơ khám theo bệnh nhân
     */
    List<HoSoKham> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm hồ sơ khám theo bác sĩ
     */
    List<HoSoKham> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm hồ sơ khám theo ngày khám
     */
    List<HoSoKham> findByNgayKham(LocalDate ngayKham);
    
    /**
     * Tìm hồ sơ khám theo khoảng thời gian
     */
    List<HoSoKham> findByNgayKhamBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm hồ sơ khám theo bệnh nhân và bác sĩ
     */
    List<HoSoKham> findByBenhnhan_BenhnhanIdAndBacsi_BacsiId(Integer benhnhanId, Integer bacsiId);
    
    /**
     * Tìm hồ sơ khám theo bệnh nhân và ngày khám
     */
    List<HoSoKham> findByBenhnhan_BenhnhanIdAndNgayKham(Integer benhnhanId, LocalDate ngayKham);
    
    /**
     * Tìm hồ sơ khám theo bác sĩ và ngày khám
     */
    List<HoSoKham> findByBacsi_BacsiIdAndNgayKham(Integer bacsiId, LocalDate ngayKham);
    
    /**
     * Đếm hồ sơ khám theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Đếm hồ sơ khám theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Đếm hồ sơ khám theo ngày
     */
    long countByNgayKham(LocalDate ngayKham);
    
    /**
     * Tìm hồ sơ khám gần nhất của bệnh nhân
     */
    @Query("SELECT hsk FROM HoSoKham hsk WHERE hsk.benhnhan.benhnhanId = :benhnhanId ORDER BY hsk.ngayKham DESC")
    List<HoSoKham> findLatestByPatient(@Param("benhnhanId") Integer benhnhanId);
    
    /**
     * Tìm hồ sơ khám hôm nay
     */
    @Query("SELECT hsk FROM HoSoKham hsk WHERE hsk.ngayKham = CURRENT_DATE ORDER BY hsk.ngayKham DESC")
    List<HoSoKham> findTodayRecords();
    
    /**
     * Tìm hồ sơ khám theo triệu chứng chứa từ khóa
     */
    @Query("SELECT hsk FROM HoSoKham hsk WHERE hsk.trieuChung LIKE %:keyword%")
    List<HoSoKham> findByTrieuChungContaining(@Param("keyword") String keyword);
    
    /**
     * Tìm hồ sơ khám theo chẩn đoán chứa từ khóa
     */
    @Query("SELECT hsk FROM HoSoKham hsk WHERE hsk.chanDoan LIKE %:keyword%")
    List<HoSoKham> findByChanDoanContaining(@Param("keyword") String keyword);
}






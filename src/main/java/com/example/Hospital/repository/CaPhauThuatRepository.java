package com.example.Hospital.repository;

import com.example.Hospital.entity.CaPhauThuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho CaPhauThuat entity
 */
@Repository
public interface CaPhauThuatRepository extends JpaRepository<CaPhauThuat, Integer> {
    
    /**
     * Tìm ca phẫu thuật theo yêu cầu
     */
    List<CaPhauThuat> findByYeuCauPhauThuat_YcptId(Integer ycptId);
    
    /**
     * Tìm ca phẫu thuật theo bác sĩ chính
     */
    List<CaPhauThuat> findByBacsiChinh_BacsiId(Integer bacsiId);
    
    /**
     * Tìm ca phẫu thuật theo phòng phẫu thuật
     */
    List<CaPhauThuat> findByPhongPhauThuat(String phongPhauThuat);
    
    /**
     * Tìm ca phẫu thuật theo ngày giờ
     */
    List<CaPhauThuat> findByNgayGio(LocalDateTime ngayGio);
    
    /**
     * Tìm ca phẫu thuật theo khoảng thời gian
     */
    List<CaPhauThuat> findByNgayGioBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm ca phẫu thuật theo phòng và ngày
     */
    @Query("SELECT cpt FROM CaPhauThuat cpt WHERE cpt.phongPhauThuat = :phongPhauThuat AND DATE(cpt.ngayGio) = DATE(:ngay)")
    List<CaPhauThuat> findByPhongAndDate(@Param("phongPhauThuat") String phongPhauThuat, @Param("ngay") LocalDateTime ngay);
    
    /**
     * Tìm ca phẫu thuật theo bác sĩ và ngày
     */
    @Query("SELECT cpt FROM CaPhauThuat cpt WHERE cpt.bacsiChinh.bacsiId = :bacsiId AND DATE(cpt.ngayGio) = DATE(:ngay)")
    List<CaPhauThuat> findByBacsiAndDate(@Param("bacsiId") Integer bacsiId, @Param("ngay") LocalDateTime ngay);
    
    /**
     * Đếm ca phẫu thuật theo bác sĩ
     */
    long countByBacsiChinh_BacsiId(Integer bacsiId);
    
    /**
     * Đếm ca phẫu thuật theo phòng
     */
    long countByPhongPhauThuat(String phongPhauThuat);
    
    /**
     * Tìm ca phẫu thuật hôm nay
     */
    @Query("SELECT cpt FROM CaPhauThuat cpt WHERE DATE(cpt.ngayGio) = CURRENT_DATE ORDER BY cpt.ngayGio ASC")
    List<CaPhauThuat> findTodaySurgeries();
    
    /**
     * Tìm ca phẫu thuật sắp tới
     */
    @Query("SELECT cpt FROM CaPhauThuat cpt WHERE cpt.ngayGio > CURRENT_TIMESTAMP ORDER BY cpt.ngayGio ASC")
    List<CaPhauThuat> findUpcomingSurgeries();
    
    /**
     * Tìm bác sĩ có nhiều ca phẫu thuật nhất
     */
    @Query("SELECT cpt.bacsiChinh FROM CaPhauThuat cpt GROUP BY cpt.bacsiChinh ORDER BY COUNT(cpt) DESC")
    List<Object[]> findDoctorsWithMostSurgeries();
}




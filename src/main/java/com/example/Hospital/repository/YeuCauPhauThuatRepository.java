package com.example.Hospital.repository;

import com.example.Hospital.entity.YeuCauPhauThuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho YeuCauPhauThuat entity
 */
@Repository
public interface YeuCauPhauThuatRepository extends JpaRepository<YeuCauPhauThuat, Integer> {
    
    /**
     * Tìm yêu cầu phẫu thuật theo bệnh nhân
     */
    List<YeuCauPhauThuat> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm yêu cầu phẫu thuật theo bác sĩ chỉ định
     */
    List<YeuCauPhauThuat> findByBacsiChiDinh_BacsiId(Integer bacsiId);
    
    /**
     * Tìm yêu cầu phẫu thuật theo trạng thái
     */
    List<YeuCauPhauThuat> findByTinhTrang(YeuCauPhauThuat.TinhTrang tinhTrang);
    
    /**
     * Tìm yêu cầu phẫu thuật theo ngày dự kiến
     */
    List<YeuCauPhauThuat> findByNgayDuKien(LocalDate ngayDuKien);
    
    /**
     * Tìm yêu cầu phẫu thuật theo khoảng thời gian
     */
    List<YeuCauPhauThuat> findByNgayDuKienBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm yêu cầu phẫu thuật theo loại phẫu thuật
     */
    List<YeuCauPhauThuat> findByLoaiPhauThuat(String loaiPhauThuat);
    
    /**
     * Tìm yêu cầu phẫu thuật theo bệnh nhân và trạng thái
     */
    List<YeuCauPhauThuat> findByBenhnhan_BenhnhanIdAndTinhTrang(Integer benhnhanId, YeuCauPhauThuat.TinhTrang tinhTrang);
    
    /**
     * Tìm yêu cầu phẫu thuật theo bác sĩ và trạng thái
     */
    List<YeuCauPhauThuat> findByBacsiChiDinh_BacsiIdAndTinhTrang(Integer bacsiId, YeuCauPhauThuat.TinhTrang tinhTrang);
    
    /**
     * Đếm yêu cầu phẫu thuật theo trạng thái
     */
    long countByTinhTrang(YeuCauPhauThuat.TinhTrang tinhTrang);
    
    /**
     * Đếm yêu cầu phẫu thuật theo bác sĩ
     */
    long countByBacsiChiDinh_BacsiId(Integer bacsiId);
    
    /**
     * Đếm yêu cầu phẫu thuật theo loại
     */
    long countByLoaiPhauThuat(String loaiPhauThuat);
    
    /**
     * Tìm yêu cầu phẫu thuật sắp tới
     */
    @Query("SELECT ycpt FROM YeuCauPhauThuat ycpt WHERE ycpt.ngayDuKien > CURRENT_DATE AND ycpt.tinhTrang = 'da_duyet' ORDER BY ycpt.ngayDuKien ASC")
    List<YeuCauPhauThuat> findUpcomingSurgeries();
    
    /**
     * Tìm yêu cầu phẫu thuật hôm nay
     */
    @Query("SELECT ycpt FROM YeuCauPhauThuat ycpt WHERE ycpt.ngayDuKien = CURRENT_DATE AND ycpt.tinhTrang = 'da_duyet'")
    List<YeuCauPhauThuat> findTodaySurgeries();
}

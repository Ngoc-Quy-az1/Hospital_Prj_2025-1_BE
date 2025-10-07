package com.example.Hospital.repository;

import com.example.Hospital.entity.DatLichKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho DatLichKham entity
 */
@Repository
public interface DatLichKhamRepository extends JpaRepository<DatLichKham, Integer> {
    
    /**
     * Tìm lịch khám theo bệnh nhân
     */
    List<DatLichKham> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm lịch khám theo bác sĩ
     */
    List<DatLichKham> findByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm lịch khám theo trạng thái
     */
    List<DatLichKham> findByTrangThai(DatLichKham.TrangThaiTrangThai trangThai);
    
    /**
     * Tìm lịch khám theo ngày giờ
     */
    List<DatLichKham> findByNgayGio(LocalDateTime ngayGio);
    
    /**
     * Tìm lịch khám theo khoảng thời gian
     */
    List<DatLichKham> findByNgayGioBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm lịch khám theo loại khám
     */
    List<DatLichKham> findByLoaiKham(String loaiKham);
    
    /**
     * Tìm lịch khám theo bệnh nhân và trạng thái
     */
    List<DatLichKham> findByBenhnhan_BenhnhanIdAndTrangThai(Integer benhnhanId, DatLichKham.TrangThaiTrangThai trangThai);
    
    /**
     * Tìm lịch khám theo bác sĩ và trạng thái
     */
    List<DatLichKham> findByBacsi_BacsiIdAndTrangThai(Integer bacsiId, DatLichKham.TrangThaiTrangThai trangThai);
    
    /**
     * Tìm lịch khám theo bác sĩ và ngày
     */
    @Query("SELECT dlk FROM DatLichKham dlk WHERE dlk.bacsi.bacsiId = :bacsiId AND DATE(dlk.ngayGio) = DATE(:ngay)")
    List<DatLichKham> findByBacsiAndDate(@Param("bacsiId") Integer bacsiId, @Param("ngay") LocalDateTime ngay);
    
    /**
     * Đếm lịch khám theo trạng thái
     */
    long countByTrangThai(DatLichKham.TrangThaiTrangThai trangThai);
    
    /**
     * Đếm lịch khám theo bác sĩ
     */
    long countByBacsi_BacsiId(Integer bacsiId);
    
    /**
     * Tìm lịch khám sắp tới của bệnh nhân
     */
    @Query("SELECT dlk FROM DatLichKham dlk WHERE dlk.benhnhan.benhnhanId = :benhnhanId AND dlk.ngayGio > CURRENT_TIMESTAMP ORDER BY dlk.ngayGio ASC")
    List<DatLichKham> findUpcomingAppointmentsByPatient(@Param("benhnhanId") Integer benhnhanId);
    
    /**
     * Tìm lịch khám hôm nay của bác sĩ
     */
    @Query("SELECT dlk FROM DatLichKham dlk WHERE dlk.bacsi.bacsiId = :bacsiId AND DATE(dlk.ngayGio) = CURRENT_DATE ORDER BY dlk.ngayGio ASC")
    List<DatLichKham> findTodayAppointmentsByDoctor(@Param("bacsiId") Integer bacsiId);
}






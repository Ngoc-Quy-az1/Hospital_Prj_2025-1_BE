package com.example.Hospital.repository;

import com.example.Hospital.entity.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho BenhNhan entity
 */
@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, Integer> {
    
    /**
     * Tìm bệnh nhân theo họ tên
     */
    List<BenhNhan> findByHoTenContainingIgnoreCase(String hoTen);
    
    /**
     * Tìm bệnh nhân theo số điện thoại
     */
    Optional<BenhNhan> findBySdt(String sdt);
    
    /**
     * Tìm bệnh nhân theo email
     */
    Optional<BenhNhan> findByEmail(String email);
    
    /**
     * Tìm bệnh nhân theo giới tính
     */
    List<BenhNhan> findByGioiTinh(BenhNhan.GioiTinh gioiTinh);
    
    /**
     * Tìm bệnh nhân theo ngày sinh
     */
    List<BenhNhan> findByNgaySinh(LocalDate ngaySinh);
    
    /**
     * Tìm bệnh nhân theo khoảng tuổi
     */
    @Query("SELECT bn FROM BenhNhan bn WHERE bn.ngaySinh BETWEEN :endDate AND :startDate")
    List<BenhNhan> findByAgeRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Tìm bệnh nhân theo địa chỉ
     */
    List<BenhNhan> findByDiaChiContainingIgnoreCase(String diaChi);
    
    /**
     * Đếm số bệnh nhân theo giới tính
     */
    long countByGioiTinh(BenhNhan.GioiTinh gioiTinh);
    
    /**
     * Đếm số bệnh nhân theo năm sinh
     */
    @Query("SELECT COUNT(bn) FROM BenhNhan bn WHERE YEAR(bn.ngaySinh) = :year")
    long countByBirthYear(@Param("year") int year);
    
    /**
     * Tìm bệnh nhân có nhiều lịch khám nhất
     */
    @Query("SELECT bn FROM BenhNhan bn ORDER BY SIZE(bn.danhSachDatLich) DESC")
    List<BenhNhan> findBenhNhanWithMostAppointments();
    
    /**
     * Tìm bệnh nhân mới đăng ký trong khoảng thời gian
     */
    @Query("SELECT bn FROM BenhNhan bn WHERE bn.benhnhanId IN " +
           "(SELECT u.benhnhan.benhnhanId FROM Users u WHERE u.userId IN " +
           "(SELECT us.user.userId FROM UserSessions us WHERE us.createdAt BETWEEN :startDate AND :endDate))")
    List<BenhNhan> findNewPatientsInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}






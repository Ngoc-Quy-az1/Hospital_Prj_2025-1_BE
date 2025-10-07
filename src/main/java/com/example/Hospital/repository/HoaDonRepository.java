package com.example.Hospital.repository;

import com.example.Hospital.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho HoaDon entity
 */
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    
    /**
     * Tìm hóa đơn theo bệnh nhân
     */
    List<HoaDon> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm hóa đơn theo ngày lập
     */
    List<HoaDon> findByNgayLap(LocalDate ngayLap);
    
    /**
     * Tìm hóa đơn theo khoảng thời gian
     */
    List<HoaDon> findByNgayLapBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm hóa đơn theo trạng thái
     */
    List<HoaDon> findByTrangThai(HoaDon.TrangThai trangThai);
    
    /**
     * Tìm hóa đơn theo bệnh nhân và trạng thái
     */
    List<HoaDon> findByBenhnhan_BenhnhanIdAndTrangThai(Integer benhnhanId, HoaDon.TrangThai trangThai);
    
    /**
     * Tìm hóa đơn theo khoảng tiền
     */
    @Query("SELECT hd FROM HoaDon hd WHERE hd.tongTien BETWEEN :minAmount AND :maxAmount")
    List<HoaDon> findByTongTienBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Tìm hóa đơn có số tiền cao nhất
     */
    @Query("SELECT hd FROM HoaDon hd WHERE hd.tongTien = (SELECT MAX(hd2.tongTien) FROM HoaDon hd2)")
    List<HoaDon> findHighestAmountInvoices();
    
    /**
     * Tìm hóa đơn có số tiền thấp nhất
     */
    @Query("SELECT hd FROM HoaDon hd WHERE hd.tongTien = (SELECT MIN(hd2.tongTien) FROM HoaDon hd2)")
    List<HoaDon> findLowestAmountInvoices();
    
    /**
     * Đếm hóa đơn theo trạng thái
     */
    long countByTrangThai(HoaDon.TrangThai trangThai);
    
    /**
     * Đếm hóa đơn theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Đếm hóa đơn theo ngày
     */
    long countByNgayLap(LocalDate ngayLap);
    
    /**
     * Tính tổng tiền hóa đơn theo trạng thái
     */
    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.trangThai = :trangThai")
    BigDecimal sumByTrangThai(@Param("trangThai") HoaDon.TrangThai trangThai);
    
    /**
     * Tính tổng tiền hóa đơn theo khoảng thời gian
     */
    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.ngayLap BETWEEN :startDate AND :endDate")
    BigDecimal sumByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Tính tổng tiền hóa đơn theo bệnh nhân
     */
    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.benhnhan.benhnhanId = :benhnhanId")
    BigDecimal sumByBenhnhan(@Param("benhnhanId") Integer benhnhanId);
    
    /**
     * Tìm bệnh nhân có tổng tiền hóa đơn cao nhất
     */
    @Query("SELECT hd.benhnhan FROM HoaDon hd GROUP BY hd.benhnhan ORDER BY SUM(hd.tongTien) DESC")
    List<Object[]> findPatientsWithHighestTotalAmount();
}






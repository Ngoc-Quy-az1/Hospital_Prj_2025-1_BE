package com.example.Hospital.repository;

import com.example.Hospital.entity.DonHangThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho DonHangThuoc entity
 */
@Repository
public interface DonHangThuocRepository extends JpaRepository<DonHangThuoc, Integer> {
    
    /**
     * Tìm đơn hàng theo bệnh nhân
     */
    List<DonHangThuoc> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm đơn hàng theo ngày đặt
     */
    List<DonHangThuoc> findByNgayDat(LocalDateTime ngayDat);
    
    /**
     * Tìm đơn hàng theo khoảng thời gian
     */
    List<DonHangThuoc> findByNgayDatBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm đơn hàng theo trạng thái
     */
    List<DonHangThuoc> findByTrangThai(DonHangThuoc.TrangThai trangThai);
    
    /**
     * Tìm đơn hàng theo bệnh nhân và trạng thái
     */
    List<DonHangThuoc> findByBenhnhan_BenhnhanIdAndTrangThai(Integer benhnhanId, DonHangThuoc.TrangThai trangThai);
    
    /**
     * Tìm đơn hàng theo khoảng tiền
     */
    @Query("SELECT dht FROM DonHangThuoc dht WHERE dht.tongTien BETWEEN :minAmount AND :maxAmount")
    List<DonHangThuoc> findByTongTienBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Đếm đơn hàng theo trạng thái
     */
    long countByTrangThai(DonHangThuoc.TrangThai trangThai);
    
    /**
     * Đếm đơn hàng theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tính tổng tiền đơn hàng theo trạng thái
     */
    @Query("SELECT SUM(dht.tongTien) FROM DonHangThuoc dht WHERE dht.trangThai = :trangThai")
    BigDecimal sumByTrangThai(@Param("trangThai") DonHangThuoc.TrangThai trangThai);
    
    /**
     * Tính tổng tiền đơn hàng theo khoảng thời gian
     */
    @Query("SELECT SUM(dht.tongTien) FROM DonHangThuoc dht WHERE dht.ngayDat BETWEEN :startDateTime AND :endDateTime")
    BigDecimal sumByDateRange(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
    
    /**
     * Tìm đơn hàng gần nhất của bệnh nhân
     */
    @Query("SELECT dht FROM DonHangThuoc dht WHERE dht.benhnhan.benhnhanId = :benhnhanId ORDER BY dht.ngayDat DESC")
    List<DonHangThuoc> findLatestByPatient(@Param("benhnhanId") Integer benhnhanId);
}




package com.example.Hospital.repository;

import com.example.Hospital.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface cho ThanhToan entity
 */
@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    
    /**
     * Tìm thanh toán theo user
     */
    List<ThanhToan> findByUser_UserId(Integer userId);
    
    /**
     * Tìm thanh toán theo loại giao dịch
     */
    List<ThanhToan> findByLoaiGiaoDich(ThanhToan.LoaiGiaoDich loaiGiaoDich);
    
    /**
     * Tìm thanh toán theo phương thức
     */
    List<ThanhToan> findByPhuongThuc(ThanhToan.PhuongThuc phuongThuc);
    
    /**
     * Tìm thanh toán theo trạng thái
     */
    List<ThanhToan> findByTrangThai(ThanhToan.TrangThai trangThai);
    
    /**
     * Tìm thanh toán theo khoảng thời gian
     */
    List<ThanhToan> findByNgayGioBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Tìm thanh toán theo khoảng tiền
     */
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.soTien BETWEEN :minAmount AND :maxAmount")
    List<ThanhToan> findBySoTienBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Tìm thanh toán theo user và trạng thái
     */
    List<ThanhToan> findByUser_UserIdAndTrangThai(Integer userId, ThanhToan.TrangThai trangThai);
    
    /**
     * Tìm thanh toán theo user và loại giao dịch
     */
    List<ThanhToan> findByUser_UserIdAndLoaiGiaoDich(Integer userId, ThanhToan.LoaiGiaoDich loaiGiaoDich);
    
    /**
     * Tìm thanh toán thành công
     */
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.trangThai = 'thanh_cong'")
    List<ThanhToan> findSuccessfulPayments();
    
    /**
     * Tìm thanh toán thất bại
     */
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.trangThai = 'that_bai'")
    List<ThanhToan> findFailedPayments();
    
    /**
     * Tìm thanh toán đang chờ xử lý
     */
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.trangThai = 'cho_xu_ly'")
    List<ThanhToan> findPendingPayments();
    
    /**
     * Đếm thanh toán theo trạng thái
     */
    long countByTrangThai(ThanhToan.TrangThai trangThai);
    
    /**
     * Đếm thanh toán theo loại giao dịch
     */
    long countByLoaiGiaoDich(ThanhToan.LoaiGiaoDich loaiGiaoDich);
    
    /**
     * Đếm thanh toán theo phương thức
     */
    long countByPhuongThuc(ThanhToan.PhuongThuc phuongThuc);
    
    /**
     * Đếm thanh toán theo user
     */
    long countByUser_UserId(Integer userId);
    
    /**
     * Tính tổng tiền thanh toán theo trạng thái
     */
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.trangThai = :trangThai")
    BigDecimal sumByTrangThai(@Param("trangThai") ThanhToan.TrangThai trangThai);
    
    /**
     * Tính tổng tiền thanh toán theo khoảng thời gian
     */
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.ngayGio BETWEEN :startDateTime AND :endDateTime")
    BigDecimal sumByDateRange(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
    
    /**
     * Tính tổng tiền thanh toán theo user
     */
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.user.userId = :userId")
    BigDecimal sumByUser(@Param("userId") Integer userId);
    
    /**
     * Tìm user có tổng tiền thanh toán cao nhất
     */
    @Query("SELECT tt.user FROM ThanhToan tt GROUP BY tt.user ORDER BY SUM(tt.soTien) DESC")
    List<Object[]> findUsersWithHighestPaymentAmount();
}




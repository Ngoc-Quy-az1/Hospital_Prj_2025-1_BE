package com.example.Hospital.repository;

import com.example.Hospital.entity.BaoCaoYTe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho BaoCaoYTe entity
 */
@Repository
public interface BaoCaoYTeRepository extends JpaRepository<BaoCaoYTe, Integer> {
    
    /**
     * Tìm báo cáo theo ngày tạo
     */
    List<BaoCaoYTe> findByNgayTao(LocalDate ngayTao);
    
    /**
     * Tìm báo cáo theo khoảng thời gian
     */
    List<BaoCaoYTe> findByNgayTaoBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm báo cáo theo loại
     */
    List<BaoCaoYTe> findByLoaiBaoCao(BaoCaoYTe.LoaiBaoCao loaiBaoCao);
    
    /**
     * Tìm báo cáo theo người tạo
     */
    List<BaoCaoYTe> findByNguoiTao_NhanvienId(Integer nhanvienId);
    
    /**
     * Tìm báo cáo theo loại và ngày tạo
     */
    List<BaoCaoYTe> findByLoaiBaoCaoAndNgayTao(BaoCaoYTe.LoaiBaoCao loaiBaoCao, LocalDate ngayTao);
    
    /**
     * Tìm báo cáo theo người tạo và khoảng thời gian
     */
    List<BaoCaoYTe> findByNguoiTao_NhanvienIdAndNgayTaoBetween(Integer nhanvienId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Đếm báo cáo theo loại
     */
    long countByLoaiBaoCao(BaoCaoYTe.LoaiBaoCao loaiBaoCao);
    
    /**
     * Đếm báo cáo theo người tạo
     */
    long countByNguoiTao_NhanvienId(Integer nhanvienId);
    
    /**
     * Đếm báo cáo theo ngày
     */
    long countByNgayTao(LocalDate ngayTao);
    
    /**
     * Tìm báo cáo gần nhất
     */
    @Query("SELECT bcyt FROM BaoCaoYTe bcyt ORDER BY bcyt.ngayTao DESC")
    List<BaoCaoYTe> findLatestReports();
    
    /**
     * Tìm báo cáo theo loại gần nhất
     */
    @Query("SELECT bcyt FROM BaoCaoYTe bcyt WHERE bcyt.loaiBaoCao = :loaiBaoCao ORDER BY bcyt.ngayTao DESC")
    List<BaoCaoYTe> findLatestReportsByType(@Param("loaiBaoCao") BaoCaoYTe.LoaiBaoCao loaiBaoCao);
}






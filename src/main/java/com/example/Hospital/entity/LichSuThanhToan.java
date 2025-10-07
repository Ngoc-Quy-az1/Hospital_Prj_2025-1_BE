package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_thanh_toan")
public class LichSuThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ls_id")
    private Integer lsId;
    
    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;
    
    @Column(name = "trang_thai_moi", length = 50)
    private String trangThaiMoi;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thanhtoan_id", nullable = false)
    private ThanhToan thanhToan;
    
    // Constructors, getters, setters
    public LichSuThanhToan() {}
    
    public Integer getLsId() { return lsId; }
    public void setLsId(Integer lsId) { this.lsId = lsId; }
    
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
    
    public String getTrangThaiMoi() { return trangThaiMoi; }
    public void setTrangThaiMoi(String trangThaiMoi) { this.trangThaiMoi = trangThaiMoi; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public ThanhToan getThanhToan() { return thanhToan; }
    public void setThanhToan(ThanhToan thanhToan) { this.thanhToan = thanhToan; }
}






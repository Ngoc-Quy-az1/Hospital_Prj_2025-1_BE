package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hoa_don")
public class HoaDon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoadon_id")
    private Integer hoadonId;
    
    @Column(name = "ngay_lap", nullable = false)
    private LocalDate ngayLap;
    
    @Column(name = "tong_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.chua_thanh_toan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    public enum TrangThai {
        chua_thanh_toan, da_thanh_toan
    }
    
    // Constructors, getters, setters
    public HoaDon() {}
    
    public Integer getHoadonId() { return hoadonId; }
    public void setHoadonId(Integer hoadonId) { this.hoadonId = hoadonId; }
    
    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public TrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThai trangThai) { this.trangThai = trangThai; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
}




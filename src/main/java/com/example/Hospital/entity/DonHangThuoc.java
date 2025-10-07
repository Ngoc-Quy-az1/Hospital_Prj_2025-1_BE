package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "don_hang_thuoc")
public class DonHangThuoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donhang_id")
    private Integer donhangId;
    
    @Column(name = "ngay_dat", nullable = false)
    private LocalDateTime ngayDat;
    
    @Column(name = "tong_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.cho_xu_ly;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @OneToMany(mappedBy = "donHangThuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonHangChiTiet> danhSachChiTiet;
    
    public enum TrangThai {
        cho_xu_ly, dang_giao, da_giao, huy
    }
    
    // Constructors, getters, setters
    public DonHangThuoc() {}
    
    public Integer getDonhangId() { return donhangId; }
    public void setDonhangId(Integer donhangId) { this.donhangId = donhangId; }
    
    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public TrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThai trangThai) { this.trangThai = trangThai; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public List<DonHangChiTiet> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<DonHangChiTiet> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }
}






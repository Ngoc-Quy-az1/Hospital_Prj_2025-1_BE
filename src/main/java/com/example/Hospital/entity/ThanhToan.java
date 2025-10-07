package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thanhtoan_id")
    private Integer thanhtoanId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_giao_dich", nullable = false)
    private LoaiGiaoDich loaiGiaoDich;
    
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", nullable = false)
    private PhuongThuc phuongThuc;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.cho_xu_ly;
    
    @Column(name = "ngay_gio", nullable = false)
    private LocalDateTime ngayGio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    
    @OneToMany(mappedBy = "thanhToan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichSuThanhToan> danhSachLichSu;
    
    public enum LoaiGiaoDich {
        vien_phi, thuoc_online
    }
    
    public enum PhuongThuc {
        tien_mat, the_tin_dung, chuyen_khoan, vi_dien_tu
    }
    
    public enum TrangThai {
        cho_xu_ly, thanh_cong, that_bai
    }
    
    // Constructors, getters, setters
    public ThanhToan() {}
    
    public Integer getThanhtoanId() { return thanhtoanId; }
    public void setThanhtoanId(Integer thanhtoanId) { this.thanhtoanId = thanhtoanId; }
    
    public LoaiGiaoDich getLoaiGiaoDich() { return loaiGiaoDich; }
    public void setLoaiGiaoDich(LoaiGiaoDich loaiGiaoDich) { this.loaiGiaoDich = loaiGiaoDich; }
    
    public BigDecimal getSoTien() { return soTien; }
    public void setSoTien(BigDecimal soTien) { this.soTien = soTien; }
    
    public PhuongThuc getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(PhuongThuc phuongThuc) { this.phuongThuc = phuongThuc; }
    
    public TrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThai trangThai) { this.trangThai = trangThai; }
    
    public LocalDateTime getNgayGio() { return ngayGio; }
    public void setNgayGio(LocalDateTime ngayGio) { this.ngayGio = ngayGio; }
    
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    
    public List<LichSuThanhToan> getDanhSachLichSu() { return danhSachLichSu; }
    public void setDanhSachLichSu(List<LichSuThanhToan> danhSachLichSu) { this.danhSachLichSu = danhSachLichSu; }
}






package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dat_lich_kham")
public class DatLichKham {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datlich_id")
    private Integer datlichId;
    
    @Column(name = "ngay_gio", nullable = false)
    private LocalDateTime ngayGio;
    
    @Column(name = "loai_kham", nullable = false, length = 100)
    private String loaiKham;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiTrangThai trangThai = TrangThaiTrangThai.cho_duyet;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id")
    private BacSi bacsi;
    
    public enum TrangThaiTrangThai {
        cho_duyet, da_duyet, da_kham, huy
    }
    
    // Constructors, getters, setters
    public DatLichKham() {}
    
    // Getters and Setters
    public Integer getDatlichId() { return datlichId; }
    public void setDatlichId(Integer datlichId) { this.datlichId = datlichId; }
    
    public LocalDateTime getNgayGio() { return ngayGio; }
    public void setNgayGio(LocalDateTime ngayGio) { this.ngayGio = ngayGio; }
    
    public String getLoaiKham() { return loaiKham; }
    public void setLoaiKham(String loaiKham) { this.loaiKham = loaiKham; }
    
    public TrangThaiTrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiTrangThai trangThai) { this.trangThai = trangThai; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
}

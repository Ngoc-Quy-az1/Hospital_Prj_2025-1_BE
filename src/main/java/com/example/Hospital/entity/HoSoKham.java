package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ho_so_kham")
public class HoSoKham {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosokham_id")
    private Integer hosokhamId;
    
    @Column(name = "ngay_kham", nullable = false)
    private LocalDate ngayKham;
    
    @Column(name = "trieu_chung", columnDefinition = "TEXT")
    private String trieuChung;
    
    @Column(name = "chan_doan", columnDefinition = "TEXT")
    private String chanDoan;
    
    @Column(name = "huong_dieu_tri", columnDefinition = "TEXT")
    private String huongDieuTri;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id", nullable = false)
    private BacSi bacsi;
    
    @OneToMany(mappedBy = "hoSoKham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BenhAn> danhSachBenhAn;
    
    // Constructors, getters, setters
    public HoSoKham() {}
    
    public Integer getHosokhamId() { return hosokhamId; }
    public void setHosokhamId(Integer hosokhamId) { this.hosokhamId = hosokhamId; }
    
    public LocalDate getNgayKham() { return ngayKham; }
    public void setNgayKham(LocalDate ngayKham) { this.ngayKham = ngayKham; }
    
    public String getTrieuChung() { return trieuChung; }
    public void setTrieuChung(String trieuChung) { this.trieuChung = trieuChung; }
    
    public String getChanDoan() { return chanDoan; }
    public void setChanDoan(String chanDoan) { this.chanDoan = chanDoan; }
    
    public String getHuongDieuTri() { return huongDieuTri; }
    public void setHuongDieuTri(String huongDieuTri) { this.huongDieuTri = huongDieuTri; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
    
    public java.util.List<BenhAn> getDanhSachBenhAn() { return danhSachBenhAn; }
    public void setDanhSachBenhAn(java.util.List<BenhAn> danhSachBenhAn) { this.danhSachBenhAn = danhSachBenhAn; }
}


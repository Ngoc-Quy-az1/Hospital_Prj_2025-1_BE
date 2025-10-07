package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "don_thuoc")
public class DonThuoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donthuoc_id")
    private Integer donthuocId;
    
    @Column(name = "ngay_ke", nullable = false)
    private LocalDate ngayKe;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id", nullable = false)
    private BacSi bacsi;
    
    @OneToMany(mappedBy = "donThuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuocChiTiet> danhSachChiTiet;
    
    @OneToMany(mappedBy = "donThuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BenhAn> danhSachBenhAn;
    
    // Constructors, getters, setters
    public DonThuoc() {}
    
    public Integer getDonthuocId() { return donthuocId; }
    public void setDonthuocId(Integer donthuocId) { this.donthuocId = donthuocId; }
    
    public LocalDate getNgayKe() { return ngayKe; }
    public void setNgayKe(LocalDate ngayKe) { this.ngayKe = ngayKe; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
    
    public List<DonThuocChiTiet> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<DonThuocChiTiet> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }
    
    public List<BenhAn> getDanhSachBenhAn() { return danhSachBenhAn; }
    public void setDanhSachBenhAn(List<BenhAn> danhSachBenhAn) { this.danhSachBenhAn = danhSachBenhAn; }
}




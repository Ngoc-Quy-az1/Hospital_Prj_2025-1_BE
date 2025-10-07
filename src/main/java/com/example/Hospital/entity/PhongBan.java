package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity quản lý thông tin phòng ban trong bệnh viện
 */
@Entity
@Table(name = "phongban")
public class PhongBan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phongban_id")
    private Integer phongbanId;
    
    @Column(name = "ten_phongban", nullable = false, length = 100)
    private String tenPhongban;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @OneToMany(mappedBy = "phongban", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NhanVien> danhSachNhanVien;
    
    @OneToMany(mappedBy = "phongban", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BacSi> danhSachBacSi;
    
    @OneToMany(mappedBy = "phongban", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichTrucBan> danhSachLichTruc;
    
    // Constructors
    public PhongBan() {}
    
    public PhongBan(String tenPhongban, String moTa) {
        this.tenPhongban = tenPhongban;
        this.moTa = moTa;
    }
    
    // Getters and Setters
    public Integer getPhongbanId() { return phongbanId; }
    public void setPhongbanId(Integer phongbanId) { this.phongbanId = phongbanId; }
    
    public String getTenPhongban() { return tenPhongban; }
    public void setTenPhongban(String tenPhongban) { this.tenPhongban = tenPhongban; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public List<NhanVien> getDanhSachNhanVien() { return danhSachNhanVien; }
    public void setDanhSachNhanVien(List<NhanVien> danhSachNhanVien) { this.danhSachNhanVien = danhSachNhanVien; }
    
    public List<BacSi> getDanhSachBacSi() { return danhSachBacSi; }
    public void setDanhSachBacSi(List<BacSi> danhSachBacSi) { this.danhSachBacSi = danhSachBacSi; }
    
    public List<LichTrucBan> getDanhSachLichTruc() { return danhSachLichTruc; }
    public void setDanhSachLichTruc(List<LichTrucBan> danhSachLichTruc) { this.danhSachLichTruc = danhSachLichTruc; }
}
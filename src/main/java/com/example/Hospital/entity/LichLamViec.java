package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lich_lam_viec")
public class LichLamViec {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lich_id")
    private Integer lichId;
    
    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;
    
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;
    
    @Column(name = "ca_lam", length = 50)
    private String caLam;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id")
    private BacSi bacsi;
    
    // Constructors, getters, setters
    public LichLamViec() {}
    
    public Integer getLichId() { return lichId; }
    public void setLichId(Integer lichId) { this.lichId = lichId; }
    
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    
    public String getCaLam() { return caLam; }
    public void setCaLam(String caLam) { this.caLam = caLam; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
}






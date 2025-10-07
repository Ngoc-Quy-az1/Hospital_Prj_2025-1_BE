package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lichtrucban")
public class LichTrucBan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lichtruc_id")
    private Integer lichtrucId;
    
    @Column(name = "ngay_truc", nullable = false)
    private LocalDate ngayTruc;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id", nullable = false)
    private BacSi bacsi;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phongban_id", nullable = false)
    private PhongBan phongban;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khunggiotruc_id", nullable = false)
    private KhungGioTrucBan khungGioTruc;
    
    // Constructors, getters, setters
    public LichTrucBan() {}
    
    public Integer getLichtrucId() { return lichtrucId; }
    public void setLichtrucId(Integer lichtrucId) { this.lichtrucId = lichtrucId; }
    
    public LocalDate getNgayTruc() { return ngayTruc; }
    public void setNgayTruc(LocalDate ngayTruc) { this.ngayTruc = ngayTruc; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
    
    public PhongBan getPhongban() { return phongban; }
    public void setPhongban(PhongBan phongban) { this.phongban = phongban; }
    
    public KhungGioTrucBan getKhungGioTruc() { return khungGioTruc; }
    public void setKhungGioTruc(KhungGioTrucBan khungGioTruc) { this.khungGioTruc = khungGioTruc; }
}


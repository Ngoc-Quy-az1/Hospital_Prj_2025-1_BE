package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "don_thuoc_chi_tiet")
public class DonThuocChiTiet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
    
    @Column(name = "lieu_dung", length = 200)
    private String lieuDung;

    @Column(name = "don_gia", precision = 15, scale = 2)
    private BigDecimal donGia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donthuoc_id", nullable = false)
    private DonThuoc donThuoc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thuoc_id", nullable = false)
    private Thuoc thuoc;
    
    // Constructors, getters, setters
    public DonThuocChiTiet() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
    
    public String getLieuDung() { return lieuDung; }
    public void setLieuDung(String lieuDung) { this.lieuDung = lieuDung; }

    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    
    public DonThuoc getDonThuoc() { return donThuoc; }
    public void setDonThuoc(DonThuoc donThuoc) { this.donThuoc = donThuoc; }
    
    public Thuoc getThuoc() { return thuoc; }
    public void setThuoc(Thuoc thuoc) { this.thuoc = thuoc; }
}






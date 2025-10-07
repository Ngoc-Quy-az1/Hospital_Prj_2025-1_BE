package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "don_hang_chi_tiet")
public class DonHangChiTiet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
    
    @Column(name = "don_gia", nullable = false, precision = 15, scale = 2)
    private BigDecimal donGia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donhang_id", nullable = false)
    private DonHangThuoc donHangThuoc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thuoc_id", nullable = false)
    private Thuoc thuoc;
    
    // Constructors, getters, setters
    public DonHangChiTiet() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
    
    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    
    public DonHangThuoc getDonHangThuoc() { return donHangThuoc; }
    public void setDonHangThuoc(DonHangThuoc donHangThuoc) { this.donHangThuoc = donHangThuoc; }
    
    public Thuoc getThuoc() { return thuoc; }
    public void setThuoc(Thuoc thuoc) { this.thuoc = thuoc; }
}




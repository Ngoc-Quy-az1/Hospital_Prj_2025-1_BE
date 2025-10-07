package com.example.Hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kho_thuoc")
public class KhoThuoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kho_id")
    private Integer khoId;
    
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
    
    @Column(name = "vi_tri", length = 100)
    private String viTri;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thuoc_id", nullable = false)
    private Thuoc thuoc;
    
    // Constructors, getters, setters
    public KhoThuoc() {}
    
    public Integer getKhoId() { return khoId; }
    public void setKhoId(Integer khoId) { this.khoId = khoId; }
    
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
    
    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }
    
    public Thuoc getThuoc() { return thuoc; }
    public void setThuoc(Thuoc thuoc) { this.thuoc = thuoc; }
}




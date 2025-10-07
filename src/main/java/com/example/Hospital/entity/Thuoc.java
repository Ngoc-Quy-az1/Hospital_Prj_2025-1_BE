package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "thuoc")
public class Thuoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thuoc_id")
    private Integer thuocId;
    
    @Column(name = "ten_thuoc", nullable = false, length = 200)
    private String tenThuoc;
    
    @Column(name = "hoat_chat", columnDefinition = "TEXT")
    private String hoatChat;
    
    @Column(name = "ham_luong", length = 100)
    private String hamLuong;
    
    @Column(name = "dang_bao_che", length = 100)
    private String dangBaoChe;
    
    @Column(name = "nha_san_xuat", length = 200)
    private String nhaSanXuat;
    
    @Column(name = "han_su_dung")
    private LocalDate hanSuDung;
    
    @OneToMany(mappedBy = "thuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuocChiTiet> danhSachDonThuocChiTiet;
    
    @OneToMany(mappedBy = "thuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<KhoThuoc> danhSachKhoThuoc;
    
    @OneToMany(mappedBy = "thuoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonHangChiTiet> danhSachDonHangChiTiet;
    
    // Constructors, getters, setters
    public Thuoc() {}
    
    public Integer getThuocId() { return thuocId; }
    public void setThuocId(Integer thuocId) { this.thuocId = thuocId; }
    
    public String getTenThuoc() { return tenThuoc; }
    public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }
    
    public String getHoatChat() { return hoatChat; }
    public void setHoatChat(String hoatChat) { this.hoatChat = hoatChat; }
    
    public String getHamLuong() { return hamLuong; }
    public void setHamLuong(String hamLuong) { this.hamLuong = hamLuong; }
    
    public String getDangBaoChe() { return dangBaoChe; }
    public void setDangBaoChe(String dangBaoChe) { this.dangBaoChe = dangBaoChe; }
    
    public String getNhaSanXuat() { return nhaSanXuat; }
    public void setNhaSanXuat(String nhaSanXuat) { this.nhaSanXuat = nhaSanXuat; }
    
    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }
    
    public List<DonThuocChiTiet> getDanhSachDonThuocChiTiet() { return danhSachDonThuocChiTiet; }
    public void setDanhSachDonThuocChiTiet(List<DonThuocChiTiet> danhSachDonThuocChiTiet) { this.danhSachDonThuocChiTiet = danhSachDonThuocChiTiet; }
    
    public List<KhoThuoc> getDanhSachKhoThuoc() { return danhSachKhoThuoc; }
    public void setDanhSachKhoThuoc(List<KhoThuoc> danhSachKhoThuoc) { this.danhSachKhoThuoc = danhSachKhoThuoc; }
    
    public List<DonHangChiTiet> getDanhSachDonHangChiTiet() { return danhSachDonHangChiTiet; }
    public void setDanhSachDonHangChiTiet(List<DonHangChiTiet> danhSachDonHangChiTiet) { this.danhSachDonHangChiTiet = danhSachDonHangChiTiet; }
}






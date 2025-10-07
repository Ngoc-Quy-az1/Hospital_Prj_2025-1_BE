package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity quản lý thông tin bác sĩ trong bệnh viện
 */
@Entity
@Table(name = "bacsi")
public class BacSi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bacsi_id")
    private Integer bacsiId;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "chuyen_khoa", length = 100)
    private String chuyenKhoa;
    
    @Column(name = "sdt", length = 20)
    private String sdt;
    
    @Column(name = "email", length = 100)
    private String email;
    
    // Quan hệ Many-to-One với PhongBan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phongban_id")
    private PhongBan phongban;
    
    // Quan hệ One-to-Many với DatLichKham
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DatLichKham> danhSachDatLich;
    
    // Quan hệ One-to-Many với HoSoKham
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoSoKham> danhSachHoSoKham;
    
    // Quan hệ One-to-Many với LichTrucBan
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichTrucBan> danhSachLichTruc;
    
    // Quan hệ One-to-Many với LichLamViec
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichLamViec> danhSachLichLamViec;
    
    // Quan hệ One-to-Many với DonThuoc
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuoc> danhSachDonThuoc;
    
    // Quan hệ One-to-Many với LabTest
    @OneToMany(mappedBy = "bacsi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabTest> danhSachLabTest;
    
    // Quan hệ One-to-Many với YeuCauPhauThuat (bác sĩ chỉ định)
    @OneToMany(mappedBy = "bacsiChiDinh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<YeuCauPhauThuat> danhSachYeuCauPhauThuat;
    
    // Quan hệ One-to-Many với CaPhauThuat (bác sĩ chính)
    @OneToMany(mappedBy = "bacsiChinh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaPhauThuat> danhSachCaPhauThuat;
    
    // Constructors
    public BacSi() {}
    
    public BacSi(String hoTen, String chuyenKhoa, PhongBan phongban, String sdt, String email) {
        this.hoTen = hoTen;
        this.chuyenKhoa = chuyenKhoa;
        this.phongban = phongban;
        this.sdt = sdt;
        this.email = email;
    }
    
    // Getters and Setters
    public Integer getBacsiId() {
        return bacsiId;
    }
    
    public void setBacsiId(Integer bacsiId) {
        this.bacsiId = bacsiId;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getChuyenKhoa() {
        return chuyenKhoa;
    }
    
    public void setChuyenKhoa(String chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }
    
    public String getSdt() {
        return sdt;
    }
    
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public PhongBan getPhongban() {
        return phongban;
    }
    
    public void setPhongban(PhongBan phongban) {
        this.phongban = phongban;
    }
    
    public List<DatLichKham> getDanhSachDatLich() {
        return danhSachDatLich;
    }
    
    public void setDanhSachDatLich(List<DatLichKham> danhSachDatLich) {
        this.danhSachDatLich = danhSachDatLich;
    }
    
    public List<HoSoKham> getDanhSachHoSoKham() {
        return danhSachHoSoKham;
    }
    
    public void setDanhSachHoSoKham(List<HoSoKham> danhSachHoSoKham) {
        this.danhSachHoSoKham = danhSachHoSoKham;
    }
    
    public List<LichTrucBan> getDanhSachLichTruc() {
        return danhSachLichTruc;
    }
    
    public void setDanhSachLichTruc(List<LichTrucBan> danhSachLichTruc) {
        this.danhSachLichTruc = danhSachLichTruc;
    }
    
    public List<LichLamViec> getDanhSachLichLamViec() {
        return danhSachLichLamViec;
    }
    
    public void setDanhSachLichLamViec(List<LichLamViec> danhSachLichLamViec) {
        this.danhSachLichLamViec = danhSachLichLamViec;
    }
    
    public List<DonThuoc> getDanhSachDonThuoc() {
        return danhSachDonThuoc;
    }
    
    public void setDanhSachDonThuoc(List<DonThuoc> danhSachDonThuoc) {
        this.danhSachDonThuoc = danhSachDonThuoc;
    }
    
    public List<LabTest> getDanhSachLabTest() {
        return danhSachLabTest;
    }
    
    public void setDanhSachLabTest(List<LabTest> danhSachLabTest) {
        this.danhSachLabTest = danhSachLabTest;
    }
    
    public List<YeuCauPhauThuat> getDanhSachYeuCauPhauThuat() {
        return danhSachYeuCauPhauThuat;
    }
    
    public void setDanhSachYeuCauPhauThuat(List<YeuCauPhauThuat> danhSachYeuCauPhauThuat) {
        this.danhSachYeuCauPhauThuat = danhSachYeuCauPhauThuat;
    }
    
    public List<CaPhauThuat> getDanhSachCaPhauThuat() {
        return danhSachCaPhauThuat;
    }
    
    public void setDanhSachCaPhauThuat(List<CaPhauThuat> danhSachCaPhauThuat) {
        this.danhSachCaPhauThuat = danhSachCaPhauThuat;
    }
}

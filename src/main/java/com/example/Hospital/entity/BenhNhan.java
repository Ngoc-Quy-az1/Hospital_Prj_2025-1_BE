package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity quản lý thông tin bệnh nhân
 */
@Entity
@Table(name = "benhnhan")
public class BenhNhan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benhnhan_id")
    private Integer benhnhanId;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh")
    private GioiTinh gioiTinh;
    
    @Column(name = "dia_chi", length = 300)
    private String diaChi;
    
    @Column(name = "sdt", length = 20)
    private String sdt;
    
    @Column(name = "email", length = 100)
    private String email;
    
    // Quan hệ One-to-Many với DatLichKham
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DatLichKham> danhSachDatLich;
    
    // Quan hệ One-to-Many với HoSoKham
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoSoKham> danhSachHoSoKham;
    
    // Quan hệ One-to-Many với DonThuoc
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuoc> danhSachDonThuoc;
    
    // Quan hệ One-to-Many với LabTest
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabTest> danhSachLabTest;
    
    // Quan hệ One-to-Many với YeuCauPhauThuat
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<YeuCauPhauThuat> danhSachYeuCauPhauThuat;
    
    // Quan hệ One-to-Many với BenhAn
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BenhAn> danhSachBenhAn;
    
    // Quan hệ One-to-Many với HoaDon
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoaDon> danhSachHoaDon;
    
    // Quan hệ One-to-Many với Users
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> danhSachUsers;
    
    // Quan hệ One-to-Many với DonHangThuoc
    @OneToMany(mappedBy = "benhnhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonHangThuoc> danhSachDonHangThuoc;
    
    // Enum cho giới tính
    public enum GioiTinh {
        Nam, Nữ, Khác
    }
    
    // Constructors
    public BenhNhan() {}
    
    public BenhNhan(String hoTen, LocalDate ngaySinh, GioiTinh gioiTinh, String diaChi, String sdt, String email) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
    }
    
    // Getters and Setters
    public Integer getBenhnhanId() {
        return benhnhanId;
    }
    
    public void setBenhnhanId(Integer benhnhanId) {
        this.benhnhanId = benhnhanId;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public LocalDate getNgaySinh() {
        return ngaySinh;
    }
    
    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    
    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }
    
    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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
    
    public List<BenhAn> getDanhSachBenhAn() {
        return danhSachBenhAn;
    }
    
    public void setDanhSachBenhAn(List<BenhAn> danhSachBenhAn) {
        this.danhSachBenhAn = danhSachBenhAn;
    }
    
    public List<HoaDon> getDanhSachHoaDon() {
        return danhSachHoaDon;
    }
    
    public void setDanhSachHoaDon(List<HoaDon> danhSachHoaDon) {
        this.danhSachHoaDon = danhSachHoaDon;
    }
    
    public List<Users> getDanhSachUsers() {
        return danhSachUsers;
    }
    
    public void setDanhSachUsers(List<Users> danhSachUsers) {
        this.danhSachUsers = danhSachUsers;
    }
    
    public List<DonHangThuoc> getDanhSachDonHangThuoc() {
        return danhSachDonHangThuoc;
    }
    
    public void setDanhSachDonHangThuoc(List<DonHangThuoc> danhSachDonHangThuoc) {
        this.danhSachDonHangThuoc = danhSachDonHangThuoc;
    }
}


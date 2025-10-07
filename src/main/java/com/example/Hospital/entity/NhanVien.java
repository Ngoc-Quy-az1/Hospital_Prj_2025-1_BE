package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity quản lý thông tin nhân viên bệnh viện
 */
@Entity
@Table(name = "nhanvien")
public class NhanVien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhanvien_id")
    private Integer nhanvienId;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "chuc_vu", length = 100)
    private String chucVu;
    
    @Column(name = "ngay_vao_lam")
    private LocalDate ngayVaoLam;
    
    @Column(name = "luong", precision = 15, scale = 2)
    private BigDecimal luong;
    
    // Quan hệ Many-to-One với PhongBan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phongban_id")
    private PhongBan phongban;
    
    // Quan hệ One-to-Many với Users
    @OneToMany(mappedBy = "nhanvien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> danhSachUsers;
    
    // Quan hệ One-to-Many với BaoCaoYTe
    @OneToMany(mappedBy = "nguoiTao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaoCaoYTe> danhSachBaoCao;
    
    // Quan hệ One-to-Many với AuditLog
    @OneToMany(mappedBy = "nhanvien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AuditLog> danhSachAuditLog;
    
    // Constructors
    public NhanVien() {}
    
    public NhanVien(String hoTen, String chucVu, PhongBan phongban, LocalDate ngayVaoLam, BigDecimal luong) {
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.phongban = phongban;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
    }
    
    // Getters and Setters
    public Integer getNhanvienId() {
        return nhanvienId;
    }
    
    public void setNhanvienId(Integer nhanvienId) {
        this.nhanvienId = nhanvienId;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getChucVu() {
        return chucVu;
    }
    
    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    
    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }
    
    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    
    public BigDecimal getLuong() {
        return luong;
    }
    
    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }
    
    public PhongBan getPhongban() {
        return phongban;
    }
    
    public void setPhongban(PhongBan phongban) {
        this.phongban = phongban;
    }
    
    public List<Users> getDanhSachUsers() {
        return danhSachUsers;
    }
    
    public void setDanhSachUsers(List<Users> danhSachUsers) {
        this.danhSachUsers = danhSachUsers;
    }
    
    public List<BaoCaoYTe> getDanhSachBaoCao() {
        return danhSachBaoCao;
    }
    
    public void setDanhSachBaoCao(List<BaoCaoYTe> danhSachBaoCao) {
        this.danhSachBaoCao = danhSachBaoCao;
    }
    
    public List<AuditLog> getDanhSachAuditLog() {
        return danhSachAuditLog;
    }
    
    public void setDanhSachAuditLog(List<AuditLog> danhSachAuditLog) {
        this.danhSachAuditLog = danhSachAuditLog;
    }
}


package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity quản lý thông tin người dùng hệ thống
 */
@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.active;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id")
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanvien;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSessions> danhSachSessions;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ThanhToan> danhSachThanhToan;
    
    public enum TrangThai {
        active, inactive
    }
    
    // Constructors
    public Users() {}
    
    public Users(String username, String passwordHash, String email, Roles role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public TrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThai trangThai) { this.trangThai = trangThai; }
    
    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public NhanVien getNhanvien() { return nhanvien; }
    public void setNhanvien(NhanVien nhanvien) { this.nhanvien = nhanvien; }
    
    public List<UserSessions> getDanhSachSessions() { return danhSachSessions; }
    public void setDanhSachSessions(List<UserSessions> danhSachSessions) { this.danhSachSessions = danhSachSessions; }
    
    public List<ThanhToan> getDanhSachThanhToan() { return danhSachThanhToan; }
    public void setDanhSachThanhToan(List<ThanhToan> danhSachThanhToan) { this.danhSachThanhToan = danhSachThanhToan; }
}

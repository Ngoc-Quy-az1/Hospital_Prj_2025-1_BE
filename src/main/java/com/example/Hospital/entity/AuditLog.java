package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;
    
    @Column(name = "hanh_dong", nullable = false, length = 255)
    private String hanhDong;
    
    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;
    
    @Column(name = "doi_tuong", length = 100)
    private String doiTuong;
    
    @Column(name = "doi_tuong_id")
    private Integer doiTuongId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanvien;
    
    // Constructors, getters, setters
    public AuditLog() {}
    
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }
    
    public String getHanhDong() { return hanhDong; }
    public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }
    
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
    
    public String getDoiTuong() { return doiTuong; }
    public void setDoiTuong(String doiTuong) { this.doiTuong = doiTuong; }
    
    public Integer getDoiTuongId() { return doiTuongId; }
    public void setDoiTuongId(Integer doiTuongId) { this.doiTuongId = doiTuongId; }
    
    public NhanVien getNhanvien() { return nhanvien; }
    public void setNhanvien(NhanVien nhanvien) { this.nhanvien = nhanvien; }
}






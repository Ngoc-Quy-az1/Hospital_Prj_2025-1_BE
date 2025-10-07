package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "yeu_cau_phau_thuat")
public class YeuCauPhauThuat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ycpt_id")
    private Integer ycptId;
    
    @Column(name = "ngay_du_kien", nullable = false)
    private LocalDate ngayDuKien;
    
    @Column(name = "loai_phau_thuat", nullable = false, length = 200)
    private String loaiPhauThuat;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang")
    private TinhTrang tinhTrang = TinhTrang.cho_duyet;
    
    @Column(name = "ly_do_tu_choi", columnDefinition = "TEXT")
    private String lyDoTuChoi;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_chi_dinh_id", nullable = false)
    private BacSi bacsiChiDinh;
    
    @OneToMany(mappedBy = "yeuCauPhauThuat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaPhauThuat> danhSachCaPhauThuat;
    
    public enum TinhTrang {
        cho_duyet, da_duyet, tu_choi, hoan_thanh
    }
    
    // Constructors, getters, setters
    public YeuCauPhauThuat() {}
    
    public Integer getYcptId() { return ycptId; }
    public void setYcptId(Integer ycptId) { this.ycptId = ycptId; }
    
    public LocalDate getNgayDuKien() { return ngayDuKien; }
    public void setNgayDuKien(LocalDate ngayDuKien) { this.ngayDuKien = ngayDuKien; }
    
    public String getLoaiPhauThuat() { return loaiPhauThuat; }
    public void setLoaiPhauThuat(String loaiPhauThuat) { this.loaiPhauThuat = loaiPhauThuat; }
    
    public TinhTrang getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(TinhTrang tinhTrang) { this.tinhTrang = tinhTrang; }
    
    public String getLyDoTuChoi() { return lyDoTuChoi; }
    public void setLyDoTuChoi(String lyDoTuChoi) { this.lyDoTuChoi = lyDoTuChoi; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public BacSi getBacsiChiDinh() { return bacsiChiDinh; }
    public void setBacsiChiDinh(BacSi bacsiChiDinh) { this.bacsiChiDinh = bacsiChiDinh; }
    
    public List<CaPhauThuat> getDanhSachCaPhauThuat() { return danhSachCaPhauThuat; }
    public void setDanhSachCaPhauThuat(List<CaPhauThuat> danhSachCaPhauThuat) { this.danhSachCaPhauThuat = danhSachCaPhauThuat; }
}






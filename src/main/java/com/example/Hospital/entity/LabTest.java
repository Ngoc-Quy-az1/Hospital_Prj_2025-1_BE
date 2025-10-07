package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lab_test")
public class LabTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "labtest_id")
    private Integer labtestId;
    
    @Column(name = "ngay_test", nullable = false)
    private LocalDate ngayTest;
    
    @Column(name = "loai_xet_nghiem", length = 200)
    private String loaiXetNghiem;
    
    @Column(name = "ket_qua", columnDefinition = "TEXT")
    private String ketQua;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_id")
    private BacSi bacsi;
    
    @OneToMany(mappedBy = "labTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BenhAn> danhSachBenhAn;
    
    // Constructors, getters, setters
    public LabTest() {}
    
    public Integer getLabtestId() { return labtestId; }
    public void setLabtestId(Integer labtestId) { this.labtestId = labtestId; }
    
    public LocalDate getNgayTest() { return ngayTest; }
    public void setNgayTest(LocalDate ngayTest) { this.ngayTest = ngayTest; }
    
    public String getLoaiXetNghiem() { return loaiXetNghiem; }
    public void setLoaiXetNghiem(String loaiXetNghiem) { this.loaiXetNghiem = loaiXetNghiem; }
    
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public BacSi getBacsi() { return bacsi; }
    public void setBacsi(BacSi bacsi) { this.bacsi = bacsi; }
    
    public java.util.List<BenhAn> getDanhSachBenhAn() { return danhSachBenhAn; }
    public void setDanhSachBenhAn(java.util.List<BenhAn> danhSachBenhAn) { this.danhSachBenhAn = danhSachBenhAn; }
}




package com.example.Hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "benh_an")
public class BenhAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benhan_id")
    private Integer benhanId;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benhnhan_id", nullable = false)
    private BenhNhan benhnhan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosokham_id")
    private HoSoKham hoSoKham;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donthuoc_id")
    private DonThuoc donThuoc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labtest_id")
    private LabTest labTest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ca_id")
    private CaPhauThuat caPhauThuat;
    
    // Constructors, getters, setters
    public BenhAn() {}
    
    public Integer getBenhanId() { return benhanId; }
    public void setBenhanId(Integer benhanId) { this.benhanId = benhanId; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public BenhNhan getBenhnhan() { return benhnhan; }
    public void setBenhnhan(BenhNhan benhnhan) { this.benhnhan = benhnhan; }
    
    public HoSoKham getHoSoKham() { return hoSoKham; }
    public void setHoSoKham(HoSoKham hoSoKham) { this.hoSoKham = hoSoKham; }
    
    public DonThuoc getDonThuoc() { return donThuoc; }
    public void setDonThuoc(DonThuoc donThuoc) { this.donThuoc = donThuoc; }
    
    public LabTest getLabTest() { return labTest; }
    public void setLabTest(LabTest labTest) { this.labTest = labTest; }
    
    public CaPhauThuat getCaPhauThuat() { return caPhauThuat; }
    public void setCaPhauThuat(CaPhauThuat caPhauThuat) { this.caPhauThuat = caPhauThuat; }
}

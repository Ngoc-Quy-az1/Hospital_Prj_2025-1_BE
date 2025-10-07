package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ca_phau_thuat")
public class CaPhauThuat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id")
    private Integer caId;
    
    @Column(name = "phong_phau_thuat", nullable = false, length = 50)
    private String phongPhauThuat;
    
    @Column(name = "ngay_gio", nullable = false)
    private LocalDateTime ngayGio;
    
    @Column(name = "kip_mo", columnDefinition = "TEXT")
    private String kipMo;
    
    @Column(name = "ket_qua", columnDefinition = "TEXT")
    private String ketQua;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ycpt_id", nullable = false)
    private YeuCauPhauThuat yeuCauPhauThuat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bacsi_chinh_id", nullable = false)
    private BacSi bacsiChinh;
    
    @OneToMany(mappedBy = "caPhauThuat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BenhAn> danhSachBenhAn;
    
    // Constructors, getters, setters
    public CaPhauThuat() {}
    
    public Integer getCaId() { return caId; }
    public void setCaId(Integer caId) { this.caId = caId; }
    
    public String getPhongPhauThuat() { return phongPhauThuat; }
    public void setPhongPhauThuat(String phongPhauThuat) { this.phongPhauThuat = phongPhauThuat; }
    
    public LocalDateTime getNgayGio() { return ngayGio; }
    public void setNgayGio(LocalDateTime ngayGio) { this.ngayGio = ngayGio; }
    
    public String getKipMo() { return kipMo; }
    public void setKipMo(String kipMo) { this.kipMo = kipMo; }
    
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
    
    public YeuCauPhauThuat getYeuCauPhauThuat() { return yeuCauPhauThuat; }
    public void setYeuCauPhauThuat(YeuCauPhauThuat yeuCauPhauThuat) { this.yeuCauPhauThuat = yeuCauPhauThuat; }
    
    public BacSi getBacsiChinh() { return bacsiChinh; }
    public void setBacsiChinh(BacSi bacsiChinh) { this.bacsiChinh = bacsiChinh; }
    
    public java.util.List<BenhAn> getDanhSachBenhAn() { return danhSachBenhAn; }
    public void setDanhSachBenhAn(java.util.List<BenhAn> danhSachBenhAn) { this.danhSachBenhAn = danhSachBenhAn; }
}






package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bao_cao_yte")
public class BaoCaoYTe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "baocao_id")
    private Integer baocaoId;
    
    @Column(name = "ngay_tao", nullable = false)
    private LocalDate ngayTao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_bao_cao", nullable = false)
    private LoaiBaoCao loaiBaoCao;
    
    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao")
    private NhanVien nguoiTao;
    
    public enum LoaiBaoCao {
        hoat_dong, tai_chinh, nhan_su, benh_nhan, phau_thuat
    }
    
    // Constructors, getters, setters
    public BaoCaoYTe() {}
    
    public Integer getBaocaoId() { return baocaoId; }
    public void setBaocaoId(Integer baocaoId) { this.baocaoId = baocaoId; }
    
    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }
    
    public LoaiBaoCao getLoaiBaoCao() { return loaiBaoCao; }
    public void setLoaiBaoCao(LoaiBaoCao loaiBaoCao) { this.loaiBaoCao = loaiBaoCao; }
    
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    
    public NhanVien getNguoiTao() { return nguoiTao; }
    public void setNguoiTao(NhanVien nguoiTao) { this.nguoiTao = nguoiTao; }
}



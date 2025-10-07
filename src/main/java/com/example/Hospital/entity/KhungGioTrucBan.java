package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "khunggiotrucban")
public class KhungGioTrucBan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khunggiotruc_id")
    private Integer khunggiotrucId;
    
    @Column(name = "khunggiotruc", nullable = false)
    private LocalTime khunggiotruc;
    
    @OneToMany(mappedBy = "khungGioTruc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichTrucBan> danhSachLichTruc;
    
    // Constructors, getters, setters
    public KhungGioTrucBan() {}
    
    public Integer getKhunggiotrucId() { return khunggiotrucId; }
    public void setKhunggiotrucId(Integer khunggiotrucId) { this.khunggiotrucId = khunggiotrucId; }
    
    public LocalTime getKhunggiotruc() { return khunggiotruc; }
    public void setKhunggiotruc(LocalTime khunggiotruc) { this.khunggiotruc = khunggiotruc; }
    
    public List<LichTrucBan> getDanhSachLichTruc() { return danhSachLichTruc; }
    public void setDanhSachLichTruc(List<LichTrucBan> danhSachLichTruc) { this.danhSachLichTruc = danhSachLichTruc; }
}


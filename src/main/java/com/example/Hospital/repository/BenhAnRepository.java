package com.example.Hospital.repository;

import com.example.Hospital.entity.BenhAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface cho BenhAn entity
 */
@Repository
public interface BenhAnRepository extends JpaRepository<BenhAn, Integer> {
    
    /**
     * Tìm bệnh án theo bệnh nhân
     */
    List<BenhAn> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm bệnh án theo hồ sơ khám
     */
    BenhAn findByHoSoKham_HosokhamId(Integer hosokhamId);
    
    /**
     * Tìm bệnh án theo đơn thuốc
     */
    BenhAn findByDonThuoc_DonthuocId(Integer donthuocId);
    
    /**
     * Tìm bệnh án theo xét nghiệm
     */
    BenhAn findByLabTest_LabtestId(Integer labtestId);
    
    /**
     * Tìm bệnh án theo ca phẫu thuật
     */
    BenhAn findByCaPhauThuat_CaId(Integer caId);
    
    /**
     * Đếm bệnh án theo bệnh nhân
     */
    long countByBenhnhan_BenhnhanId(Integer benhnhanId);
}






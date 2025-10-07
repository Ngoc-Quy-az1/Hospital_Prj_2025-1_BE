package com.example.Hospital.repository;

import com.example.Hospital.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Roles entity
 */
@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    
    /**
     * Tìm role theo tên
     */
    Optional<Roles> findByTenRole(String tenRole);
    
    /**
     * Kiểm tra role có tồn tại theo tên
     */
    boolean existsByTenRole(String tenRole);
    
    /**
     * Tìm role theo tên chứa từ khóa
     */
    List<Roles> findByTenRoleContainingIgnoreCase(String tenRole);
    
    /**
     * Đếm số user theo role
     */
    @Query("SELECT COUNT(u) FROM Users u WHERE u.role.roleId = :roleId")
    long countUsersByRoleId(@Param("roleId") Integer roleId);
    
    /**
     * Tìm role có nhiều user nhất
     */
    @Query("SELECT r FROM Roles r ORDER BY SIZE(r.danhSachUsers) DESC")
    List<Roles> findRolesWithMostUsers();
}




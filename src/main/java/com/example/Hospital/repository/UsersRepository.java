package com.example.Hospital.repository;

import com.example.Hospital.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Users entity
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    
    /**
     * Tìm user theo username
     */
    Optional<Users> findByUsername(String username);
    
    /**
     * Tìm user theo email
     */
    Optional<Users> findByEmail(String email);
    
    /**
     * Tìm user theo role
     */
    List<Users> findByRole_RoleId(Integer roleId);
    
    /**
     * Tìm user theo trạng thái
     */
    List<Users> findByTrangThai(Users.TrangThai trangThai);
    
    /**
     * Tìm user theo bệnh nhân
     */
    Optional<Users> findByBenhnhan_BenhnhanId(Integer benhnhanId);
    
    /**
     * Tìm user theo nhân viên
     */
    Optional<Users> findByNhanvien_NhanvienId(Integer nhanvienId);
    
    /**
     * Kiểm tra username có tồn tại
     */
    boolean existsByUsername(String username);
    
    /**
     * Kiểm tra email có tồn tại
     */
    boolean existsByEmail(String email);
    
    /**
     * Đếm số user theo role
     */
    long countByRole_RoleId(Integer roleId);
    
    /**
     * Đếm số user theo trạng thái
     */
    long countByTrangThai(Users.TrangThai trangThai);
    
    /**
     * Tìm user active theo role
     */
    @Query("SELECT u FROM Users u WHERE u.role.roleId = :roleId AND u.trangThai = 'active'")
    List<Users> findActiveUsersByRole(@Param("roleId") Integer roleId);
    
    /**
     * Tìm user có nhiều session nhất
     */
    @Query("SELECT u FROM Users u ORDER BY SIZE(u.danhSachSessions) DESC")
    List<Users> findUsersWithMostSessions();
}






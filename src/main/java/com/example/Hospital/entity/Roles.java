package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity quản lý vai trò (role) của người dùng trong hệ thống
 */
@Entity
@Table(name = "roles")
public class Roles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    
    @Column(name = "ten_role", unique = true, nullable = false, length = 50)
    private String tenRole;
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> danhSachUsers;
    
    // Constructors
    public Roles() {}
    
    public Roles(String tenRole) {
        this.tenRole = tenRole;
    }
    
    // Getters và Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public String getTenRole() { return tenRole; }
    public void setTenRole(String tenRole) { this.tenRole = tenRole; }
    
    public List<Users> getDanhSachUsers() { return danhSachUsers; }
    public void setDanhSachUsers(List<Users> danhSachUsers) { this.danhSachUsers = danhSachUsers; }
}



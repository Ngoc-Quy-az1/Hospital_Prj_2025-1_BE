package com.example.Hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", nullable = false, length = 255, columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.active;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Status {
        active, expired
    }

    public OtpCode() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}


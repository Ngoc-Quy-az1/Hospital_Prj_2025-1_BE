package com.example.Hospital.repository;

import com.example.Hospital.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Integer> {

    Optional<OtpCode> findTopByUsernameAndStatusOrderByCreatedAtDesc(String username, OtpCode.Status status);
}



package com.digio.digio_clone.repository;

import com.digio.digio_clone.entity.SigningPartyOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigningPartyOtpRepository
        extends JpaRepository<SigningPartyOtp, Long> {
}
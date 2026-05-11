package com.digio.digio_clone.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "signing_parties")
@Getter
@Setter
public class SigningParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    private String reason;

    private String signType;

    private String status;

    private LocalDateTime expireOn;

    private String otp;

    private boolean otpVarified = false;

    private boolean signed = false;

    private LocalDateTime signedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
}

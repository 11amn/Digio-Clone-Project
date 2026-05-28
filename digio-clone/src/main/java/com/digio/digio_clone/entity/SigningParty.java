package com.digio.digio_clone.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "signing_parties")
@Getter
@Setter
public class SigningParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String identifier;

    private String status;

    private Boolean signed = false;

    private LocalDateTime expireOn;

    private LocalDateTime signedAt;

    private String reason;

    private String signType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @OneToMany(
            mappedBy = "signingParty",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SigningPartyOtp> otps =
            new ArrayList<>();
}

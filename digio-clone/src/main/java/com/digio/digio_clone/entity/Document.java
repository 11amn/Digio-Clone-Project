package com.digio.digio_clone.entity;


import com.digio.digio_clone.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String digioDocumentId;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private Integer expireInDays;

    private String displayOnPage;

    private boolean sendSignLink;

    private boolean notifySigners;

    private String agreementType;

    private Boolean isAgreement;

    private Boolean selfSigned;

    private String channel;

    private String selfSignType;

    private String signedDocUrl;

    private String unsignedDocUrl;

    private Integer noOfPages;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fileData;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SigningParty> signingParties = new ArrayList<>();

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
    private SignRequestDetails signRequestDetails;
}

package com.digio.digio_clone.Service.impl;


import com.digio.digio_clone.DTO.*;
import com.digio.digio_clone.Entity.Document;
import com.digio.digio_clone.Entity.SigningParty;
import com.digio.digio_clone.Enums.DocumentStatus;
import com.digio.digio_clone.Repository.DocumentRepository;
import com.digio.digio_clone.Service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public ApiResponse<?> uploadDocument(DigioSignRequest request) {

        // CREATE DOCUMENT
        Document document = new Document();

        document.setDigioDocumentId("DOC-" + UUID.randomUUID().toString().substring(0,8));

        document.setFileName(request.getFileName());

        document.setFileData(request.getFileData());

        document.setExpireInDays(request.getExpireInDays());

        document.setDisplayOnPage(request.getDisplayOnPage());

        document.setSendSignLink(request.getSendSignLink());

        document.setNotifySigners(request.getNotifySigners());

        document.setCreatedAt(LocalDateTime.now());

        document.setStatus(DocumentStatus.CREATED);

        // SAVE SIGNERS
        for (SignerDTO signerDTO : request.getSigners()) {

            SigningParty signingParty = new SigningParty();

            signingParty.setIdentifier(signerDTO.getIdentifier());

            signingParty.setReason(signerDTO.getReason());

            signingParty.setSignType(signerDTO.getSignType());

            signingParty.setStatus("PENDING");

            signingParty.setExpireOn(LocalDateTime.now().plusDays(request.getExpireInDays()));

            signingParty.setDocument(document);

            document.getSigningParties().add(signingParty);
        }

        // SAVE DOCUMENT
        Document savedDocument = documentRepository.save(document);

        return new ApiResponse<>(true, "Document uploaded successfully", savedDocument);
    }

    @Override
    public ApiResponse<?> getDocumentById(Long id) {

        Document document = documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        DocumentResponseDTO responseDTO = DocumentResponseDTO.builder().id(document.getId()).digioDocumentId(document.getDigioDocumentId())
                .fileName(document.getFileName())
                .status(document.getStatus().name())
                .expireInDays(document.getExpireInDays())
                .createdAt(document.getCreatedAt())
                .signers(document.getSigningParties().stream().map(signer -> SignerResponseDTO.builder()
                        .identifier(signer.getIdentifier())
                        .reason(signer.getReason())
                        .signType(signer.getSignType())
                        .status(signer.getStatus())
                        .build()).collect(Collectors.toList())).build();
        return new ApiResponse<>( true, "Document fetched successfully", responseDTO);
    }

    @Override
    public ApiResponse<?> initiateSigning(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(()
        -> new RuntimeException("Document not found"));

        // CHECK IF EXPIRED
        if (document.getCreatedAt().plusDays(document.getExpireInDays())
                .isBefore(LocalDateTime.now())) {
            document.setStatus(DocumentStatus.EXPIRED);

            documentRepository.save(document);

            throw new RuntimeException("Document Expired");
        }

        // GENERATE OTP FOR ALL SIGNERS
        for (SigningParty signer : document.getSigningParties()) {
            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            signer.setOtp(otp);

            signer.setStatus("OTP_SENT");

            System.out.println("OTP for " + signer.getIdentifier() + " : " + otp);
        }

        document.setStatus(DocumentStatus.REQUESTED);

        documentRepository.save(document);

        return new ApiResponse<>(true, "OTP generated successfully", document.getDigioDocumentId());
    }
}

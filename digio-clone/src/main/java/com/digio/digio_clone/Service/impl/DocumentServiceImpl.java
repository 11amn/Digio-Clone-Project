package com.digio.digio_clone.Service.impl;


import com.digio.digio_clone.DTO.*;
import com.digio.digio_clone.DTO.response.DigioSignResponse;
import com.digio.digio_clone.Entity.Document;
import com.digio.digio_clone.Entity.SigningParty;
import com.digio.digio_clone.Enums.DocumentStatus;
import com.digio.digio_clone.Repository.DocumentRepository;
import com.digio.digio_clone.Service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public ApiResponse<DigioSignResponse> uploadDocument(
            DigioSignRequest request
    ) {

        // DECODE BASE64 PDF
        byte[] pdfBytes;

        try {

            pdfBytes = Base64.getDecoder()
                    .decode(request.getFileData());

        } catch (Exception e) {

            throw new RuntimeException("Invalid Base64 PDF");
        }

        // VALIDATE PDF + GET PAGE COUNT
        int totalPages = getPdfPageCount(pdfBytes);

        // CREATE DOCUMENT
        Document document = new Document();

        document.setDigioDocumentId(
                "DOC-" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase()
        );

        document.setFileName(request.getFileName());

        document.setFileData(request.getFileData());

        document.setExpireInDays(request.getExpireInDays());

        document.setDisplayOnPage(request.getDisplayOnPage());

        document.setSendSignLink(request.getSendSignLink());

        document.setNotifySigners(request.getNotifySigners());

        // NEW FIELDS
        document.setAgreementType(
                request.getAgreementType()
        );

        document.setIsAgreement(
                request.getIsAgreement()
        );

        document.setSelfSigned(
                request.getSelfSigned()
        );

        document.setChannel(
                request.getChannel()
        );

        document.setSelfSignType(
                request.getSelfSignType()
        );

        document.setUnsignedDocUrl(
                "LOCAL_STORAGE"
        );

        document.setNoOfPages(totalPages);

        document.setCreatedAt(LocalDateTime.now());

        document.setStatus(DocumentStatus.CREATED);

        // SAVE SIGNERS
        for (SignerDTO signerDTO : request.getSigners()) {

            SigningParty signingParty =
                    new SigningParty();

            signingParty.setName(signerDTO.getName());

            signingParty.setIdentifier(
                    signerDTO.getIdentifier()
            );

            signingParty.setReason(
                    signerDTO.getReason()
            );

            signingParty.setSignType(
                    signerDTO.getSignType()
            );

            signingParty.setStatus("PENDING");

            signingParty.setExpireOn(
                    LocalDateTime.now()
                            .plusDays(request.getExpireInDays())
            );

            signingParty.setDocument(document);

            document.getSigningParties()
                    .add(signingParty);
        }

        // SAVE DOCUMENT
        Document savedDocument =
                documentRepository.save(document);

        DigioSignResponse response =
                mapToDigiSignResponse(savedDocument);

        return new ApiResponse<>(
                true,
                "Document uploaded successfully",
                response
        );
    }

    private DigioSignResponse mapToDigiSignResponse(
            Document savedDocument
    ) {

        DigioSignResponse response =
                new DigioSignResponse();

        response.setSigners(
                savedDocument.getSigningParties()
                        .stream()
                        .map(s -> SignerDTO.builder()
                                .signType(s.getSignType())
                                .identifier(s.getIdentifier())
                                .reason(s.getReason())
                                .status(s.getStatus())
                                .build()
                        ).toList()
        );

        response.setDisplayOnPage(
                savedDocument.getDisplayOnPage()
        );

        response.setFileName(
                savedDocument.getFileName()
        );

        response.setId(
                savedDocument.getDigioDocumentId()
        );

        response.setExpireInDays(
                savedDocument.getExpireInDays()
        );

        // ADD THESE
        response.setSendSignLink(
                savedDocument.isSendSignLink()
        );

        response.setNotifySigners(
                savedDocument.isNotifySigners()
        );

        response.setAgreementType(
                savedDocument.getAgreementType()
        );

        response.setIsAgreement(
                savedDocument.getIsAgreement()
        );

        response.setSelfSigned(
                savedDocument.getSelfSigned()
        );

        response.setChannel(
                savedDocument.getChannel()
        );

        response.setSelfSignType(
                savedDocument.getSelfSignType()
        );

        return response;
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

    @Override
    public ApiResponse<?> signDocument(Long documentId, SignDocumentRequest request) {
        Document document = documentRepository.findById(documentId).orElseThrow(()
        -> new RuntimeException("Document not found"));

        // CHECK EXPIRE
        if (document.getCreatedAt().plusDays(document.getExpireInDays()).isBefore(LocalDateTime.now())) {

            document.setStatus(DocumentStatus.EXPIRED);

            documentRepository.save(document);

            throw new RuntimeException("Document expired");
        }

        // FIND SIGNER
        SigningParty signer = document.getSigningParties().stream()
                .filter(s -> s.getIdentifier().equals(request.getIdentifier()))
                .findFirst().orElseThrow(() -> new RuntimeException("Signer not found"));

        // VERIFY OTP
        if (!signer.getOtp().equals(request.getOtp())) {

            throw new RuntimeException("Invalid OTP");
        }

        // MARK VERIFIED
        signer.setOtpVarified(true);
        signer.setSigned(true);
        signer.setSignedAt(LocalDateTime.now());
        signer.setStatus("SIGNED");

        // CHECK ALL SIGNERS
        boolean allSigned = document.getSigningParties().stream().allMatch(SigningParty::getSigned);

        if (allSigned) {
            document.setStatus(DocumentStatus.SIGNED);
        } else {
            document.setStatus(DocumentStatus.IN_PROGRESS);
        }

        documentRepository.save(document);

        return new ApiResponse<>(true, "Document signed successfully", document.getStatus());
    }

    @Override
    public byte[] downloadDocument(Long documentId) {

        Document document = documentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found"));

        // ONLY SIGNED DOCS DOWNLOADABLE
        if (!document.getStatus().equals(DocumentStatus.SIGNED)) {
            throw new RuntimeException("Document is not fully signed");
        }

        // DECODE DASE64 -> BYTE[]
        return Base64.getDecoder().decode(document.getFileData());
    }

    private int getPdfPageCount(byte[] pdfBytes) {
        try (PDDocument document =
                     Loader.loadPDF(pdfBytes)) {
            int totalPages = document.getNumberOfPages();
            log.info("PDF page count: {}", totalPages);
            return totalPages;

        } catch (IOException e) {
            log.error("Unable to read PDF pages", e);
            throw new RuntimeException("Invalid PDF document");
        }
    }

    @Override
    public byte[] downloadSignedDocument(
            Long documentId
    ) {

        Document document =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found"
                                )
                        );

        // GET SIGNER
        SigningParty signer =
                document.getSigningParties()
                        .get(0);

        // DECODE BASE64 PDF
        byte[] pdfBytes =
                Base64.getDecoder()
                        .decode(document.getFileData());

        // ADD WATERMARK
        return addSignatureWatermark(
                pdfBytes,
                signer.getName()
        );
    }

    private byte[] addSignatureWatermark(
            byte[] pdfBytes,
            String signedBy
    ) {

        try (
                PDDocument document =
                        Loader.loadPDF(pdfBytes);

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

            String signedDate = LocalDateTime.now().format(formatter);

            for (PDPage page : document.getPages()) {

                PDPageContentStream contentStream =
                        new PDPageContentStream(
                                document,
                                page,
                                PDPageContentStream.AppendMode.APPEND,
                                true
                        );

                contentStream.beginText();

                contentStream.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        10
                );

                // RIGHT SIDE POSITION
                contentStream.newLineAtOffset(
                        page.getMediaBox().getWidth() - 180,
                        20
                );

                contentStream.showText(
                        "Signed By : " + signedBy
                );

                contentStream.newLineAtOffset(
                        0,
                        -15
                );

                contentStream.showText(
                        "Date : " + signedDate
                );

                contentStream.endText();

                contentStream.close();
            }

            document.save(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to add watermark"
            );
        }
    }
}

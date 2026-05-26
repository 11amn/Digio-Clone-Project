package com.digio.digio_clone.controller;


import com.digio.digio_clone.dto.ApiResponse;
import com.digio.digio_clone.dto.DigioSignRequest;
import com.digio.digio_clone.dto.SignDocumentRequest;
import com.digio.digio_clone.dto.response.DigioSignResponse;
import com.digio.digio_clone.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ApiResponse<DigioSignResponse> uploadDocument(@RequestBody DigioSignRequest request) {
        return documentService.uploadDocument(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getDocumentById(@PathVariable String id){
        return documentService.getDocumentById(id);
    }

    @PostMapping("/{documentId}/initiate")
    public ApiResponse<?> initiateSigning(
            @PathVariable String documentId
    ) {

        return documentService
                .initiateSigning(documentId);
    }

    @PostMapping("/{documentId}/sign")
    public ApiResponse<?> signDocument(@PathVariable String documentId, @RequestBody SignDocumentRequest request) {
        return documentService.signDocument(documentId, request);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable String documentId
    ) {

        byte[] pdf =
                documentService
                        .downloadSignedDocument(documentId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=signed-document.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}

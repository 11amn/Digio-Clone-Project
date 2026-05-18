package com.digio.digio_clone.Controller;


import com.digio.digio_clone.DTO.ApiResponse;
import com.digio.digio_clone.DTO.DigioSignRequest;
import com.digio.digio_clone.DTO.SignDocumentRequest;
import com.digio.digio_clone.DTO.response.DigioSignResponse;
import com.digio.digio_clone.Service.DocumentService;
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
    public ApiResponse<?> getDocumentById(@PathVariable Long id){
        return documentService.getDocumentById(id);
    }

    @PostMapping("/{id}/initiate")
    public ApiResponse<?> initiateSigning(@PathVariable Long id) {
        return documentService.initiateSigning(id);
    }

    @PostMapping("/{id}/sign")
    public ApiResponse<?> signDocument(@PathVariable Long id, @RequestBody SignDocumentRequest request) {
        return documentService.signDocument(id, request);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long documentId
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

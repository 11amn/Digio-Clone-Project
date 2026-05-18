package com.digio.digio_clone.service;

import com.digio.digio_clone.dto.ApiResponse;
import com.digio.digio_clone.dto.DigioSignRequest;
import com.digio.digio_clone.dto.SignDocumentRequest;
import com.digio.digio_clone.dto.response.DigioSignResponse;

public interface DocumentService {

    ApiResponse<DigioSignResponse> uploadDocument(DigioSignRequest request);
    ApiResponse<?> getDocumentById(Long id);
    ApiResponse<?> initiateSigning(Long documentId);
    ApiResponse<?> signDocument(Long documentId, SignDocumentRequest request);
    byte[] downloadDocument(Long documentId);

    byte[] downloadSignedDocument(Long documentId);
}

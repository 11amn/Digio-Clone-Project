package com.digio.digio_clone.service;

import com.digio.digio_clone.dto.ApiResponse;
import com.digio.digio_clone.dto.DigioSignRequest;
import com.digio.digio_clone.dto.SignDocumentRequest;
import com.digio.digio_clone.dto.response.DigioSignResponse;

public interface DocumentService {

    ApiResponse<DigioSignResponse> uploadDocument(DigioSignRequest request);
    ApiResponse<?> getDocumentById(String id);
    ApiResponse<?> initiateSigning(String documentId);
    ApiResponse<?> signDocument(String documentId, SignDocumentRequest request);
    byte[] downloadDocument(String documentId);

    byte[] downloadSignedDocument(String documentId);
}

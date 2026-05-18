package com.digio.digio_clone.Service;

import com.digio.digio_clone.DTO.ApiResponse;
import com.digio.digio_clone.DTO.DigioSignRequest;
import com.digio.digio_clone.DTO.SignDocumentRequest;
import com.digio.digio_clone.DTO.response.DigioSignResponse;

public interface DocumentService {

    ApiResponse<DigioSignResponse> uploadDocument(DigioSignRequest request);
    ApiResponse<?> getDocumentById(Long id);
    ApiResponse<?> initiateSigning(Long documentId);
    ApiResponse<?> signDocument(Long documentId, SignDocumentRequest request);
    byte[] downloadDocument(Long documentId);

    byte[] downloadSignedDocument(Long documentId);
}

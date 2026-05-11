package com.digio.digio_clone.Service;

import com.digio.digio_clone.DTO.ApiResponse;
import com.digio.digio_clone.DTO.DigioSignRequest;

public interface DocumentService {

    ApiResponse<?> uploadDocument(DigioSignRequest request);
    ApiResponse<?> getDocumentById(Long id);
}

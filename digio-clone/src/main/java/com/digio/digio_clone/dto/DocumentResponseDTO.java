package com.digio.digio_clone.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocumentResponseDTO {

    private Long id;

    private String digioDocumentId;

    private String fileName;

    private String status;

    private Integer expireInDays;

    private LocalDateTime createdAt;

    private List<SignerResponseDTO> signers;
}

package com.digio.digio_clone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignerResponseDTO {

    private String identifier;

    private String reason;

    private String signType;

    private String status;
}

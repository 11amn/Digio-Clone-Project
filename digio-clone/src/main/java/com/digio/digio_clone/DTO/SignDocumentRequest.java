package com.digio.digio_clone.DTO;

import lombok.Data;

@Data
public class SignDocumentRequest {

    private String identifier;

    private String otp;
}

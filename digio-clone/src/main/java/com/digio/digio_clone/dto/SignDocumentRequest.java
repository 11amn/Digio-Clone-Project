package com.digio.digio_clone.dto;

import lombok.Data;

@Data
public class SignDocumentRequest {

    private String identifier;

    private String otp;
}

package com.digio.digio_clone.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignerDTO {

    private String name;

    private String identifier;

    private String reason;

    private String signType;

    private String status;
}

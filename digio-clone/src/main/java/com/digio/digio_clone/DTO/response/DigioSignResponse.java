package com.digio.digio_clone.DTO.response;

import com.digio.digio_clone.DTO.SignerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigioSignResponse {

    private String id;

    private List<SignerDTO> signers;

    private Integer expireInDays;

    private String displayOnPage;

    private Boolean sendSignLink;

    private Boolean notifySigners;

    private String fileName;

    private String agreementType;

    private Boolean isAgreement;

    private Boolean selfSigned;

    private String channel;

    private String selfSignType;
}

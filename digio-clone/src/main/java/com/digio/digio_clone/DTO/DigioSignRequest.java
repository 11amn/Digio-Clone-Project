package com.digio.digio_clone.DTO;


import lombok.Data;

import java.util.List;

@Data
public class DigioSignRequest {

    private List<SignerDTO> signers;

    private Integer expireInDays;

    private String displayOnPage;

    private Boolean sendSignLink;

    private Boolean notifySigners;

    private String fileName;

    private String fileData;
}

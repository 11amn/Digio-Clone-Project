package com.digio.digio_clone.Controller;


import com.digio.digio_clone.DTO.ApiResponse;
import com.digio.digio_clone.DTO.DigioSignRequest;
import com.digio.digio_clone.Service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ApiResponse<?> uploadDocument(@RequestBody DigioSignRequest request) {
        return documentService.uploadDocument(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getDocumentById(@PathVariable Long id){
        return documentService.getDocumentById(id);
    }

    @PostMapping("/{id}/initiate")
    public ApiResponse<?> initiateSigning(@PathVariable Long id) {
        return documentService.initiateSigning(id);
    }
}

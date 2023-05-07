package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.services.DocumentService;
import com.mayosen.letipractice.services.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(value = "/bytes/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> findDocument(@PathVariable int id) {
        Document document = documentService.findDocumentBy(id);

        ByteArrayResource resource = new ByteArrayResource(document.getBlob());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
            .attachment()
            .filename(document.getName(), StandardCharsets.UTF_8)
            .build());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable int id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }
}

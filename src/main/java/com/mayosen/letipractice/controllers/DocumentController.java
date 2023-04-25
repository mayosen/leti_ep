package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.responses.AllDocumentsResponse;
import com.mayosen.letipractice.responses.DocumentResponse;
import com.mayosen.letipractice.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addDocument(@RequestPart MultipartFile file) {
        documentService.addDocument(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateDocument(
        @PathVariable int id,
        @RequestPart MultipartFile file
    ) {
        documentService.updateDocument(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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

    @GetMapping("")
    public ResponseEntity<AllDocumentsResponse> findAllDocuments() {
        List<DocumentResponse> documents = documentService.findAllDocuments();
        return ResponseEntity.ok(new AllDocumentsResponse(documents));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable int id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }
}

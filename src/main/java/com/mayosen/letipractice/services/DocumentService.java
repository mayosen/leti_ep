package com.mayosen.letipractice.services;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.repos.DocumentRepository;
import com.mayosen.letipractice.responses.DocumentResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DocumentService {
    private final OcrService ocrService;
    private final ParsingService parsingService;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(
        OcrService ocrService,
        ParsingService parsingService,
        DocumentRepository documentRepository
    ) {
        this.ocrService = ocrService;
        this.parsingService = parsingService;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void addDocument(MultipartFile file) {
        String name = file.getOriginalFilename();
        byte[] bytes;

        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }

        String text = ocrService.extractTextFromPdf(name, bytes);
        List<String> words = parsingService.parseWords(text);
        List<String> filteredWords = parsingService.filterWords(words);
        String topWords = String.join(", ", parsingService.findTopWords(filteredWords));

        Document document = new Document();
        document.setName(name);
        // TODO: author
        document.setCreated(LocalDate.now());
        document.setUpdated(LocalDate.now());
        document.setParsedText(text);
        document.setBlob(bytes);
        document.setTopWords(topWords);

        documentRepository.save(document);
    }

    public Document findDocumentBy(int id) {
        Document document = documentRepository.findById(id).orElseThrow();
        Hibernate.initialize(document.getBlob());
        return document;
    }

    public List<DocumentResponse> findAllDocuments() {
        return documentRepository.findAll().stream()
            .map(document -> DocumentResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .author(document.getAuthor())
                .created(document.getCreated())
                .updated(document.getUpdated())
                .parsedText(document.getParsedText())
                .topWords(document.getTopWords())
                .build())
            .toList();
    }

    @Transactional
    public void deleteDocument(int id) {
        Document document = findDocumentBy(id);
        documentRepository.delete(document);
    }
}

package com.mayosen.letipractice.services;

import com.mayosen.letipractice.mappers.DocumentMapper;
import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.models.User;
import com.mayosen.letipractice.repos.DocumentRepository;
import com.mayosen.letipractice.responses.DocumentResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    public void addDocument(MultipartFile file, String name, int authorId) {
        Document document = new Document();
        document.setName(StringUtils.hasText(name) ? name : file.getOriginalFilename()) ;
        document.setAuthor(new User(authorId));
        updateDocumentFrom(document, extractBytes(file));
        LocalDate now = LocalDate.now();
        document.setCreated(now);
        document.setUpdated(now);
        documentRepository.save(document);
    }

    @Transactional
    public void updateDocument(int id, MultipartFile file, String name) {
        Document document = findDocumentBy(id);
        document.setName(StringUtils.hasText(name) ? name : file.getOriginalFilename());
        updateDocumentFrom(document, extractBytes(file));
        document.setUpdated(LocalDate.now());
        documentRepository.save(document);
    }

    private byte[] extractBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }

    private void updateDocumentFrom(Document document, byte[] bytes) {
        String text = ocrService.extractTextFromPdf(bytes);
        List<String> words = parsingService.parseWords(text);
        List<String> filteredWords = parsingService.filterWords(words);
        String topWords = String.join(", ", parsingService.findTopWords(filteredWords));

        document.setParsedText(text);
        document.setBlob(bytes);
        document.setTopWords(topWords);
    }

    public Document findDocumentBy(int id) {
        Document document = documentRepository.findById(id).orElseThrow();
        Hibernate.initialize(document.getBlob());
        return document;
    }

    public List<DocumentResponse> findAllDocuments() {
        return documentRepository.findAll().stream()
            .map(DocumentMapper::documentToResponse)
            .toList();
    }

    public List<DocumentResponse> findAllDocumentsByName(String name) {
        return documentRepository.findAllByNameContainingIgnoreCase(name).stream()
            .map(DocumentMapper::documentToResponse)
            .toList();
    }

    public List<DocumentResponse> findMyDocuments(int authorId) {
        return documentRepository.findAllByAuthorId(authorId).stream()
            .map(DocumentMapper::documentToResponse)
            .toList();
    }

    @Transactional
    public void deleteDocument(int id) {
        Document document = findDocumentBy(id);
        documentRepository.delete(document);
    }
}

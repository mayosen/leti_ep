package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.responses.DocumentResponse;
import com.mayosen.letipractice.services.DocumentService;
import com.mayosen.letipractice.services.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MainController {
    private final DocumentService documentService;

    @Autowired
    public MainController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/find")
    public String find() {
        return "find";
    }

    @GetMapping(value = "/documents")
    public String documentTable(@RequestParam(required = false) String name, Model model) {
        List<DocumentResponse> documents;
        if (name == null) {
            documents = documentService.findAllDocuments();
        } else {
            documents = documentService.findAllDocumentsByName(name);
        }
        model.addAttribute("documents", documents);
        return "documentTable";
    }

    @GetMapping("/documents/{id}")
    public String document(@PathVariable(required = true) int id, Model model) {
        Document document = documentService.findDocumentBy(id);
        model.addAttribute("document", document);
        return "document";
    }

    @GetMapping("/upload")
    public String upload(@RequestParam(required = false) String replaceId, Model model) {
        if (replaceId != null) {
            model.addAttribute("replaceId", replaceId);
        }
        return "upload";
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addDocument(
        @RequestParam("name") String name,
        @RequestParam("file") MultipartFile file,
        Authentication authentication
    ) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        documentService.addDocument(file, name, userDetails.getId());
        return "redirect:/documents";
    }

    @PostMapping(value = "/documents/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateDocument(
        @PathVariable int id,
        @RequestParam("name") String name,
        @RequestPart MultipartFile file
    ) {
        documentService.updateDocument(id, file, name);
        return "redirect:/documents";
    }
}

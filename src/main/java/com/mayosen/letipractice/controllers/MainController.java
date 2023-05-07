package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.repos.DocumentRepository;
import com.mayosen.letipractice.responses.DocumentResponse;
import com.mayosen.letipractice.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/table")
    public String table(@RequestParam(required = false) String name, Model model) {
        List<DocumentResponse> documents;
        if (name == null) {
            documents = documentService.findAllDocuments();
        } else {
            documents = documentService.findAllDocumentsByName(name);
        }
        model.addAttribute("documents", documents);
        return "table";
    }

    @GetMapping("/find")
    public String find() {
        return "find";
    }
}

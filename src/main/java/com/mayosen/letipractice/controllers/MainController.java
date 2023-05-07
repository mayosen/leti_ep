package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.responses.DocumentResponse;
import com.mayosen.letipractice.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}

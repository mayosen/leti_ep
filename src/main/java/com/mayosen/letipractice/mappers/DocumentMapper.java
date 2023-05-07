package com.mayosen.letipractice.mappers;

import com.mayosen.letipractice.models.Document;
import com.mayosen.letipractice.responses.DocumentResponse;

public interface DocumentMapper {
    static DocumentResponse documentToResponse(Document document) {
        return DocumentResponse.builder()
            .id(document.getId())
            .name(document.getName())
            .author(document.getAuthor().getLogin())
            .created(document.getCreated())
            .updated(document.getUpdated())
            .parsedText(document.getParsedText())
            .topWords(document.getTopWords())
            .build();
    }
}

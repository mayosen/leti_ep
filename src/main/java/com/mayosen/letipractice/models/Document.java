package com.mayosen.letipractice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "updated")
    private LocalDate updated;

    @Column(name = "parsed_text", columnDefinition = "text")
    private String parsedText;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "binary_data")
    private byte[] blob;

    @Column(name = "top_words")
    private String topWords;
}

package com.mayosen.letipractice.responses;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private Integer id;

    private String name;

    private String author;

    private LocalDate created;

    private LocalDate updated;

    private String parsedText;

    private String topWords;
}

package com.mayosen.letipractice;

import com.mayosen.letipractice.services.OcrService;
import com.mayosen.letipractice.services.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class LetiPracticeApplication {
    @Autowired
    OcrService ocrService;
    @Autowired
    ParsingService parsingService;

    public static void main(String[] args) {
        SpringApplication.run(LetiPracticeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        File file = new File("samples/Тестовый.pdf");

        String text = ocrService.extractTextFromPdf(file);
        System.out.println("Parsed text");
        System.out.println(text);

        List<String> words = parsingService.parseWords(text);
        System.out.println("Parsed words");
        System.out.println(words);

        List<String> filteredWords = parsingService.filterWords(words);
        System.out.println("Filtered words");
        System.out.println(filteredWords);

        List<String> topWords = parsingService.findTopWords(filteredWords);
        System.out.println("Top words");
        System.out.println(topWords);
    }
}

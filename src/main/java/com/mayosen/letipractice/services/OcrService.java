package com.mayosen.letipractice.services;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
public class OcrService {
    private final ITesseract tesseract;

    @Autowired
    public OcrService(ITesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String extractTextFromPdf(String name, byte[] file) {
        StringBuilder documentText = new StringBuilder();
        log.debug("Started scanning document '{}'", name);

        try (PDDocument document = PDDocument.load(file)) {
            PDFRenderer renderer = new PDFRenderer(document);
            log.debug("Found {} pages", document.getNumberOfPages());

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                log.debug("Scanning page #{}", page + 1);
                String pagePext = tesseract.doOCR(image);
                documentText.append(pagePext);
            }
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }

        return documentText.toString();
    }
}

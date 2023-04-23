package com.mayosen.letipractice.config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfig {
    @Bean
    public ITesseract tesseract() {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("rus+eng");
        tesseract.setOcrEngineMode(1);
        return tesseract;
    }
}

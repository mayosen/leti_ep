package com.mayosen.letipractice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParsingService {
    private static final Pattern wordPattern = Pattern.compile("([а-яА-Яa-zA-Z-]+)");
    private static final Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();

    public List<String> parseWords(String input) {
        Matcher matcher = wordPattern.matcher(input);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    public List<String> filterWords(List<String> words) {
        return words.stream().filter(word -> word.length() >= 5).map(String::toLowerCase).toList();
    }

    public List<String> findTopWords(List<String> words) {
        Map<String, Integer> counter = words.stream().collect(Collectors.toMap(
            word -> word,
            word -> 1,
            Integer::sum
        ));
        return counter.entrySet().stream()
            .sorted(comparator.reversed())
            .limit(7)
            .map(Map.Entry::getKey)
            .toList();
    }
}

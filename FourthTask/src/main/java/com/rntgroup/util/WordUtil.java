package com.rntgroup.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Stream;

@UtilityClass
public class WordUtil {
    private static final String PUNCTUATION_REGEX = "[.,?!:;']";

    public static String cleanWord(String word) {
        word = word.replaceAll("^" + PUNCTUATION_REGEX + "+" + "|" + PUNCTUATION_REGEX + "+$", "");

        if (word.matches(PUNCTUATION_REGEX)) {
            return "";
        }

        return word;
    }

    public static Stream<String> splitAndCleanWord(String line) {

        return Arrays.stream(line.split("\\s+|--"))
                .map(WordUtil::cleanWord)
                .filter(word -> !word.isEmpty());
    }
}

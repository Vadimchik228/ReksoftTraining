package com.rntgroup.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class WordUtilTest {

    @ParameterizedTest
    @MethodSource("getSplitAndCleanWordArguments")
    void splitAndCleanWord(String inputWord, String[] expectedWords) {
        String[] actualWords = WordUtil.splitAndCleanWord(inputWord).toArray(String[]::new);

        assertArrayEquals(expectedWords, actualWords);
    }

    static Stream<Arguments> getSplitAndCleanWordArguments() {
        return Stream.of(
                Arguments.of("Seems, madam!",
                        new String[]{"Seems", "madam"}),
                Arguments.of("nay it is; I know not 'seems.'",
                        new String[]{"nay", "it", "is", "I", "know", "not", "seems"}),
                Arguments.of("No, no, the drink, the drink,--O my dear Hamlet,--",
                        new String[]{"No", "no", "the", "drink", "the", "drink", "O", "my", "dear", "Hamlet"}),
                Arguments.of("Is strict in his arrest--O, I could tell you--",
                        new String[]{"Is", "strict", "in", "his", "arrest", "O", "I", "could", "tell", "you"}),
                Arguments.of("Fall'n on the inventors' reads: all this can I",
                        new String[]{"Fall'n", "on", "the", "inventors", "reads", "all", "this", "can", "I"}),
                Arguments.of("That hurts by easing. But, to the quick o' the ulcer:--",
                        new String[]{"That", "hurts", "by", "easing", "But", "to", "the", "quick", "o", "the", "ulcer"}),
                Arguments.of("'Run barefoot up and down, threatening the flames.",
                        new String[]{"Run", "barefoot", "up", "and", "down", "threatening", "the", "flames"}),
                Arguments.of("  'Tis   true. 'Tis  pity;   ",
                        new String[]{"Tis", "true", "Tis", "pity"}),
                Arguments.of("Of this his nephew's purpose,--to suppress",
                        new String[]{"Of", "this", "his", "nephew's", "purpose", "to", "suppress"}),
                Arguments.of("Thou know'st 'tis common; all that lives must die,",
                        new String[]{"Thou", "know'st", "tis", "common", "all", "that", "lives", "must", "die"}),
                Arguments.of("'This must be so.' We pray you, throw to earth",
                        new String[]{"This", "must", "be", "so", "We", "pray", "you", "throw", "to", "earth"}),
                Arguments.of("Pays homage to us--thou mayst not coldly set",
                        new String[]{"Pays", "homage", "to", "us", "thou", "mayst", "not", "coldly", "set"}),
                Arguments.of("Why yet I live to say 'This thing's to do;'",
                        new String[]{"Why", "yet", "I", "live", "to", "say", "This", "thing's", "to", "do"})
        );
    }

}

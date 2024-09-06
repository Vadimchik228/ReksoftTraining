package com.rntgroup.validator;

import com.rntgroup.web.dto.validation.validator.CorrectPasswordValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CorrectPasswordValidatorTest {

    private final CorrectPasswordValidator validator = new CorrectPasswordValidator();

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfPasswords")
    void isValidPassword(String password, boolean expectedResult) {
        boolean actualResult = validator.isValid(password, null);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    static Stream<Arguments> getValidationArgumentsOfPasswords() {
        return Stream.of(
                Arguments.of("vadimsdf", false),
                Arguments.of("V123!", false),
                Arguments.of("Vv123!", true),
                Arguments.of("1234234", false),
                Arguments.of("!#$%^&#", false),
                Arguments.of(" va2342! ", true),
                Arguments.of("ыыщщБ2342! ", true),
                Arguments.of(null, true)
        );
    }
}

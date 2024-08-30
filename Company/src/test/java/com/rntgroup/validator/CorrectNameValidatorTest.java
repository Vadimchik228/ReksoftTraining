package com.rntgroup.validator;

import com.rntgroup.web.dto.validation.validator.CorrectNameValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class CorrectNameValidatorTest {

    private final CorrectNameValidator validator = new CorrectNameValidator();

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfNames")
    void isValidName(String name, boolean expectedResult) {
        boolean actualResult = validator.isValid(name, null);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    static Stream<Arguments> getValidationArgumentsOfNames() {
        return Stream.of(
                Arguments.of("Иван", true),
                Arguments.of("Иван-Иванович", true),
                Arguments.of("John", true),
                Arguments.of("John-Doe", true),
                Arguments.of("Иванов", true),
                Arguments.of("иван", false),
                Arguments.of("john", false),
                Arguments.of("Иван1", false),
                Arguments.of("Иван_", false),
                Arguments.of("Иван!", false),
                Arguments.of("-Иван", false),
                Arguments.of("Иван Иванов", false),
                Arguments.of("John Doe", false),
                Arguments.of(null, true)
        );
    }
}

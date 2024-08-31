package com.rntgroup.validator;

import com.rntgroup.web.dto.validation.validator.CorrectPhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class CorrectPhoneNumberValidatorTest {

    private final CorrectPhoneNumberValidator validator = new CorrectPhoneNumberValidator();

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String phoneNumber, boolean expectedResult) {
        boolean actualResult = validator.isValid(phoneNumber, null);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                // Валидные номера с +7
                Arguments.of("+79161234567", true),
                Arguments.of("+7 (916) 123-45-67", true),
                Arguments.of("+7 916 123 45 67", true),
                Arguments.of("+7 916 123-45-67", true),
                Arguments.of("+7 (916) 123 45 67", true),

                // Валидные номера с 8
                Arguments.of("89161234567", true),
                Arguments.of("8 (916) 123-45-67", true),
                Arguments.of("8 916 123 45 67", true),
                Arguments.of("8 916 123-45-67", true),
                Arguments.of("8 (916) 123 45 67", true),

                // Валидные номера без +7/8
                Arguments.of("9161234567", true),
                Arguments.of("(916) 123-45-67", true),
                Arguments.of("916 123 45 67", true),
                Arguments.of("916 123-45-67", true),
                Arguments.of("(916) 123 45 67", true),

                // Невалидные номера
                Arguments.of("1234567", false),
                Arguments.of("12345678901234567", false),
                Arguments.of("123456789012345", false),
                Arguments.of("abc1234567", false),
                Arguments.of("1234567890", false),
                Arguments.of("+7 916 123 45 67 1", false), // Слишком много цифр
                Arguments.of("123-456-7890-12", false), // Слишком много дефисов
                Arguments.of("+1 555 123 45 67", false), // Не российский номер
                Arguments.of(null, false)
        );
    }
}

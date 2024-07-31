package com.rntgroup.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.WEEKS;

@UtilityClass
public class LocalDateUtil {
    public static boolean dateIsCorrectWeek(LocalDate localDate, int weeks) {
        return WEEKS.between(localDate, LocalDate.now()) <= weeks;
    }
}

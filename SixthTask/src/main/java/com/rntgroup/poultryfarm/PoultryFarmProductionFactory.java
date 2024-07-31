package com.rntgroup.poultryfarm;

import java.time.LocalDate;
import java.util.Random;

public class PoultryFarmProductionFactory {
    private static final Random RANDOM = new Random();
    private static final int MIN_NUMBER_OF_EGGS_PRODUCED = 1;
    private static final int MAX_NUMBER_OF_EGGS_PRODUCED = 20;
    private static final BirdType[] BIRD_TYPES = BirdType.values();

    private PoultryFarmProductionFactory() {
    }

    public static PoultryFarmProduction getPoultryFarmProduction() {
        return new PoultryFarmProduction(BIRD_TYPES[RANDOM.nextInt(BIRD_TYPES.length)],
                RANDOM.nextInt(MIN_NUMBER_OF_EGGS_PRODUCED, MAX_NUMBER_OF_EGGS_PRODUCED),
                getRandomDate());
    }

    private static LocalDate getRandomDate() {
        long minDay = LocalDate.of(2000, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2023, 12, 31).toEpochDay();
        long randomDay = minDay + RANDOM.nextLong() % (maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}

package com.rntgroup.poultryfarm;

import com.rntgroup.util.LocalDateUtil;

import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PoultryFarmStatistics {
    public static final int NUMBER_OF_EGGS_PRODUCED_IN_THREE_WEEKS = 20;
    private static final int THREE_WEEKS = 3;
    private final Collection<PoultryFarmProduction> poultryFarmProductions;

    public PoultryFarmStatistics(Supplier<Collection<PoultryFarmProduction>> supplier, List<PoultryFarmProduction> storage) {
        poultryFarmProductions = supplier.get();
        poultryFarmProductions.addAll(storage);
    }

    // Список типов птиц, которые хотя бы один раз произвели больше 20 яиц за последние 3 недели
    public List<BirdType> getBirdTypesByCond() {
        return poultryFarmProductions.stream()
                .filter(poultryFarmProduction -> LocalDateUtil.dateIsCorrectWeek(poultryFarmProduction.getProductionDate(), THREE_WEEKS))
                .collect(Collectors.groupingBy(
                        PoultryFarmProduction::getBirdType,
                        Collectors.summingInt(PoultryFarmProduction::getNumberOfEggsProduced)))
                .entrySet().stream()
                .filter(poultryFarmProduction -> poultryFarmProduction.getValue() > NUMBER_OF_EGGS_PRODUCED_IN_THREE_WEEKS)
                .map(Map.Entry::getKey)
                .toList();
    }

    // Самый продуктивный месяц
    public Optional<Month> getTheMostProductiveMonth() {
        return poultryFarmProductions.stream()
                .collect(Collectors.groupingBy(
                        (PoultryFarmProduction poultryFarmProduction) -> poultryFarmProduction.getProductionDate().getMonth(),
                        Collectors.summingInt(PoultryFarmProduction::getNumberOfEggsProduced)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .stream()
                .findFirst()
                .map(Map.Entry::getKey);
    }

    // Общее количество произведенных яиц
    public int getTotalNumberOfEggsProduced() {
        return poultryFarmProductions.stream()
                .mapToInt(PoultryFarmProduction::getNumberOfEggsProduced)
                .sum();
    }
}

package com.rntgroup.poultryfarm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PoultryFarmProductionListFactory {
    private static int count;

    static {
        count = 3412;
    }

    public static void setCount(int count) {
        PoultryFarmProductionListFactory.count = count;
    }

    public static List<PoultryFarmProduction> getPoultryFarmProductionList() {
        return Stream.generate(PoultryFarmProductionFactory::getPoultryFarmProduction)
                .limit(count)
                .collect(Collectors.toList());
    }
}

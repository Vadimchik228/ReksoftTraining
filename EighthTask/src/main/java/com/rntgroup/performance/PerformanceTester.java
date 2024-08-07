package com.rntgroup.performance;

import com.rntgroup.poultryfarm.PoultryFarmProduction;
import com.rntgroup.poultryfarm.PoultryFarmProductionListFactory;
import com.rntgroup.poultryfarm.PoultryFarmStatistics;

import java.util.Collection;
import java.util.function.Supplier;

public class PerformanceTester {
    private PoultryFarmStatistics poultryFarmStatistics;

    public TimeMeasurement testCollection(Supplier<Collection<PoultryFarmProduction>> supplier) {

        long creationTime = getMethodExecutionTime(() -> poultryFarmStatistics = new PoultryFarmStatistics(supplier,
                PoultryFarmProductionListFactory.getPoultryFarmProductionList()));
        long birdTypesByCondTime = getMethodExecutionTime(poultryFarmStatistics::getBirdTypesByCond);
        long theMostProductiveMonthTime = getMethodExecutionTime(poultryFarmStatistics::getTheMostProductiveMonth);
        long totalNumberOfEggsProducedTime = getMethodExecutionTime(poultryFarmStatistics::getTotalNumberOfEggsProduced);

        return new TimeMeasurement(creationTime,
                birdTypesByCondTime,
                theMostProductiveMonthTime,
                totalNumberOfEggsProducedTime);
    }

    private static long getMethodExecutionTime(Runnable method) {
        long timeBeforeExecution = System.nanoTime();
        method.run();
        return System.nanoTime() - timeBeforeExecution;
    }
}
package com.rntgroup.statistics;

import com.rntgroup.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayStatisticsTest extends TestBase {

    @Test
    public void testCountUniqueHamletWords() {
        int actualCount = PlayStatistics.countUniqueHamletWords(play);
        int expectedCount = 30;
        assertEquals(expectedCount, actualCount);
    }

}

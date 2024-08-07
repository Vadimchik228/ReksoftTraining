package com.rntgroup.performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimeMeasurement {
    private long creation;
    private long birdTypesByCond;
    private long theMostProductiveMonth;
    private long totalNumberOfEggsProduced;
}

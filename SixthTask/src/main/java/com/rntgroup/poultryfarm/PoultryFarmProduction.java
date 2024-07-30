package com.rntgroup.poultryfarm;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PoultryFarmProduction {
    private BirdType birdType;
    private int numberOfEggsProduced;
    private LocalDate productionDate;

    public PoultryFarmProduction(BirdType birdType, int numberOfEggsProduced, LocalDate productionDate) {
        this.birdType = birdType;
        this.numberOfEggsProduced = numberOfEggsProduced;
        this.productionDate = productionDate;
    }
}

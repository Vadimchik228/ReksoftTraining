package com.rntgroup.task1.entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Bet {
    private Horse horse;
    private BigDecimal amount;
}

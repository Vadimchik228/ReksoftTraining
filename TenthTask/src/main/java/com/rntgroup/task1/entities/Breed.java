package com.rntgroup.task1.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Breed {
    private String name;
    private int maxSpeed;
}

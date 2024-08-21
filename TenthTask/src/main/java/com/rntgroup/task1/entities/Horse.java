package com.rntgroup.task1.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Horse {
    private String name;
    private Breed breed;
    private int age;
    private Rider rider;
    private Integer coveredDistance;
    private Integer position;
}

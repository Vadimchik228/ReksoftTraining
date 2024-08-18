package com.rntgroup.task1.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rider {
    private String firstName;
    private String lastName;
    private int age;
}

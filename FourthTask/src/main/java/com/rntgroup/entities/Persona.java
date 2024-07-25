package com.rntgroup.entities;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class Persona {
    private String title;
    private String name;
    private String groupDescription;
}

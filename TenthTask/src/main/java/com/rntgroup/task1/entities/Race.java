package com.rntgroup.task1.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Race {
    private LocalDateTime dateTime;
    private List<Horse> participants;
    private Integer distance;
}

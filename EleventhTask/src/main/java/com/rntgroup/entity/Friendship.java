package com.rntgroup.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Friendship {
    private Long userId1;
    private Long userId2;
    private LocalDateTime timestamp;
}

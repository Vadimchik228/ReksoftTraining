package com.rntgroup.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Like {
    private Long postId;
    private Long userId;
    private LocalDateTime timestamp;
}

package com.rntgroup.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Post {
    private Long id;
    private Long userId;
    private String text;
    private LocalDateTime timestamp;
}

package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long memberId;
}
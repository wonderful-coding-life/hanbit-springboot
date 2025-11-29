package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    private String id;
    private String name;
    private String email;
    @TextIndexed(weight = 2)
    private String title;
    @TextIndexed
    private String description;
    @TextScore
    private Float score;
}

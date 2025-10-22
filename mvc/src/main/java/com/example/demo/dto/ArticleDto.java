package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ArticleDto {
    private Long id;
    private Long memberId;
    private String name;
    private String email;
    private String title;
    private String description;
    private Date created;
    private Date updated;
}

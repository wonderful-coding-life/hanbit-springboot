package com.example.demo.controller;

import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/article/list")
    public String getArticleList(@PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", articleService.findAll(pageable));
        return "article-list";
    }
}

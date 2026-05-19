package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 API", description = "게시글 조회, 등록, 수정 API")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    //@GetMapping
    public List<ArticleResponse> getAll(@RequestParam(name= "memberId", required=false) Long memberId) {
        if (memberId == null) {
            return articleService.findAll();
        } else {
            return articleService.findByMemberId(memberId);
        }
    }

    // http://localhost:8080/api/articles?page=1&size=3&sort=title,asc&sort=description,desc
    @Operation(summary = "게시글 페이지 조회", description = "페이지 크기, 페이지, 정렬 기준, 정렬 방향으로 게시글 페이지 조회")
    @GetMapping
    public Page<ArticleResponse> getAllWithPageable(@ParameterObject @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable, @Parameter(description = "작성자 아이디 없으면 전체 게시글", example = "1") @RequestParam(name= "memberId", required=false) Long memberId) {
        if (memberId == null) {
            return articleService.findAll(pageable);
        } else {
            return articleService.findByMemberId(memberId, pageable);
        }
    }

    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }

    @PutMapping("/{id}")
    public ArticleResponse put(@PathVariable("id") Long id, @RequestBody ArticleRequest articleRequest) {
        return articleService.update(id, articleRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        articleService.delete(id);
    }
}

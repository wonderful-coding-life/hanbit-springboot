package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "회원 API", description = "회원 조회, 등록, 수정 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse post(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }

    @GetMapping
    public List<MemberResponse> getAll(@RequestParam(name= "name", required=false) String name) {
        return memberService.findAll(name);
    }

    @Operation(summary = "회원 단건 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    @GetMapping("/{id}")
    public MemberResponse get(@Parameter(description = "회원 ID", example = "1") @PathVariable("id") Long id) {
        return memberService.findById(id);
    }

    //@GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getResponseEntity(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findById(id);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)) // HTTP 캐시 헤더
                .body(memberResponse);
    }

    @PutMapping("/{id}")
    public MemberResponse put(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    @PatchMapping("/{id}")
    public MemberResponse patch(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.patch(id, memberRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        memberService.deleteById(id);
    }

    @PostMapping("/{id}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse postArticle(@PathVariable("id") Long id, @RequestBody ArticleRequest articleRequest) {
        return articleService.create(id, articleRequest);
    }

    @GetMapping("/{id}/articles")
    public void getArticle(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().getServletContext().getRequestDispatcher("/api/articles?memberId=" + id).forward(request, response);
    }
}

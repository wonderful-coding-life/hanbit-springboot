package com.example.demo.service;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.exception.ArticleNotFoundException;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.model.Article;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public ArticleResponse create(Long memberId, ArticleRequest articleRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .description(articleRequest.getDescription())
                .member(member).build();
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }

    public List<ArticleResponse> findAll() {
        return articleRepository.findAll()
                .stream()
                .map(this::mapToArticleResponse)
                .toList();
    }

    public List<ArticleResponse> findByMemberId_(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        List<Article> articles = articleRepository.findAllByMember(member);
        List<ArticleResponse> result = new ArrayList<>();

        for (Article article : articles) {
            ArticleResponse response = ArticleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .created(article.getCreated())
                    .updated(article.getUpdated())
                    .memberId(article.getMember().getId())
                    .name(article.getMember().getName())
                    .email(article.getMember().getEmail())
                    .build();

            result.add(response);
        }

        return result;
    }

    public List<ArticleResponse> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return articleRepository.findAllByMember(member)
                .stream()
                .map(this::mapToArticleResponse)
                .toList();
    }

    public Page<ArticleResponse> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(this::mapToArticleResponse);
    }

    public Page<ArticleResponse> findByMemberId(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return articleRepository.findAllByMember(member, pageable)
                .map(this::mapToArticleResponse);
    }

    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        return mapToArticleResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        articleRepository.delete(article);
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail()).build();
    }
}

package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByMember(Member member);
    Page<Article> findAllByMember(Member member, Pageable pageable);

    // SimpleJpaRepository.java에 saveAll(), deleteAll()등 여러개의 row에 영향을 미치는 메서드에 @Transactional이 붙어있음
    @Transactional
    void deleteByMember(Member member);
    @Transactional
    void deleteByMemberId(Long memberId);
}

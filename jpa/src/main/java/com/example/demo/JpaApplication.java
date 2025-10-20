package com.example.demo;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(2)
public class JpaApplication implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var member = memberRepository.findMember("윤서준").getFirst();

        var article = Article.builder()
                .title("방학 첫날이다")
                .description("오늘은 열심히 방학 숙제를 했다")
                .member(member).build();
        articleRepository.save(article);

        var articles = articleRepository.findAll();
        log.info("{}", articles);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "id"));
        var page = articleRepository.findAll(pageable);
        log.info("{}", page.getNumber()); // page number
        log.info("{}", page.getNumberOfElements()); // page items
        log.info("{}", page.getTotalPages());
        log.info("{}", page.getTotalElements());

        articles = page.getContent();
        //log.info("{}", articles);
        articles.forEach(a -> log.info("{}", a));

        // 동등 비교(Equal) 기반의 탐색(탐침)
        Example<Member> probe = Example.of(
                Member.builder().name("윤").age(10).build(),
                //ExampleMatcher.matchingAll() // = matching()
                //ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //ExampleMatcher.matchingAny()
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        var members = memberRepository.findAll(probe);
        log.info("{}", members);
    }
}

package com.example.demo;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
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
        log.info("number {}", page.getNumber()); // page number
        log.info("numberOfElements {}", page.getNumberOfElements()); // page items
        log.info("totalPages {}", page.getTotalPages());
        log.info("totalElements {}", page.getTotalElements());
        log.info("hasNext {}", page.hasNext());
        log.info("hasPrevious {}", page.hasPrevious());

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

        // 영속성 컨텍스트 테스트
        // ApplicationRunner에는 영속성 컨텍스트가 없기 때문에 그냥 실행하면 다른 객체
        // 하지만 @Transactional을 클래스 또는 run 메서드에 추가하여 강제로 영속성 컨텍스트를 생성하면 동일 객체
        var m1 = memberRepository.findMember("윤서준").getFirst();
        var m2 = memberRepository.findById(1L).get();
        if (m1 == m2) {
            log.info(">>> 동일 객체 : {} {}", m1, m2);
        } else {
            log.info(">>> 다른 객체 : {} {}", m1, m2);
        }
    }
}

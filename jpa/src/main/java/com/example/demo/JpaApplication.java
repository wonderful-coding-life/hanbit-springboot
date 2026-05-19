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
        var test = memberRepository.findById(1L);
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

        // 영속성 컨텍스트(Persistence Context)
        // - 엔티티를 관리하는 JPA의 메모리 공간
        // - 1차 캐시 - 같은 PK의 엔티티 조회 시 DB를 다시 조회하지 않고 캐시된 객체 반환
        // - 동일성 보장 - 같은 영속성 컨텍스트 안에서는 같은 PK의 엔티티에 대해 항상 동일 객체 반환

        // 트랜잭션(@Transactional)
        // - 트랜잭션 시작 시 영속성 컨텍스트 생성
        // - 영속 상태(managed) 엔티티의 변경 사항을 Dirty Checking으로 추적
        // - setEmail() 등으로 엔티티 값을 변경하면
        //   트랜잭션 commit 시 flush 되면서 UPDATE SQL 실행

        // OSIV(Open Session In View)
        // - Spring Boot에서 Spring Web + JPA를 사용하면 기본적으로 활성화
        // - HTTP 요청(Request) 시작 시 영속성 컨텍스트(EntityManager) 생성
        // - View/JSON 응답 생성 시점까지 영속성 컨텍스트 유지
        // - Lazy Loading 가능
        // - 단, 트랜잭션을 자동 생성하지는 않음
        // - 따라서 Dirty Checking만으로 자동 UPDATE 되지는 않음
        // - OSIV는 영속성 컨텍스트는 유지하지만 트랜잭션은 자동 생성하지 않음

        // ApplicationRunner
        // - 웹 요청 기반이 아니므로 OSIV가 적용되지 않음
        // - 필요하다면 @Transactional을 사용하여
        //   트랜잭션과 영속성 컨텍스트를 함께 생성하여 사용

        var m1 = memberRepository.findMember("윤서준").getFirst();
        var m2 = memberRepository.findById(1L).get();
        if (m1 == m2) {
            log.info(">>> 동일 객체 : {} {}", m1, m2);
        } else {
            log.info(">>> 다른 객체 : {} {}", m1, m2);
        }

        // 트랜잭션 안에서는 save() 없이 엔티티 값만 변경해도
        // commit 시 Dirty Checking으로 UPDATE SQL 실행
        m1.setAge(99);
    }
}

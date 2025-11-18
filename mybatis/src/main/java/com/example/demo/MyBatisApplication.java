package com.example.demo;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Article;
import com.example.demo.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyBatisApplication implements ApplicationRunner {
    private final MemberMapper memberMapper;
    private final ArticleMapper articleMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 최근의 JDBC 드라이버는 디폴트로 allowMultiQueries가 false로 설정되어 있다.
        //spring.datasource.url=jdbc:mysql://localhost:3306/mydb?allowMultiQueries=true
        //List<Member> members = memberMapper.selectAllOrderBy("id; DROP TABLE member; --", "desc");
        //log.info("Member count: {}", members.size());

        int count = memberMapper.selectAllCount();
        log.info("Member count: {}", count);

        Member member = memberMapper
                .selectByEmail("SeojunYoon@hanbit.co.kr")
                .orElseThrow();
        log.info("Member: {}", member);

        Article article = Article.builder()
                .title("Hello, MyBatis")
                .description("MyBatis is an SQL Mapper framework")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .memberId(member.getId())
                .build();
        int inserted = articleMapper.insert(article);
        log.info("Inserted: rowCount {}", inserted);
        log.info("Inserted: {}", inserted);


        // subscribeBatch();
    }

    // 프록시 기반으로 내부에서 호출하는 메서드에는 트랜잭션이 걸리지 않는다.
    // 외부에서 이 MyBatisApplcation을 호출하는 run 메서드에 @Transactional을 걸어야 한다.
    //@Transactional
    public void subscribeBatch() {
        List<Member> members = List.of(
                Member.builder().name("홍길동6").email("GildongHong6@hanbit.co.kr").age(16).build(), // 재고 차감
                Member.builder().name("홍길동7").email("GildongHong7@hanbit.co.kr").age(16).build(),
                Member.builder().name("홍길동8").email("GildongHong1@hanbit.co.kr").age(16).build(), // 사용자 포인트 차감
                Member.builder().name("홍길동9").email("GildongHong9@hanbit.co.kr").age(16).build(),
                Member.builder().name("홍길동10").email("GildongHong10@hanbit.co.kr").age(16).build()
        );
        for (Member member : members) {
            memberMapper.insert(member);
            log.info("inserted {}", member);
        }
    }
}

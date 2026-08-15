package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void doBeforeEach() {
        if (articleRepository.count() > 0) {
            articleRepository.deleteAll();
        }
        if (memberRepository.count() > 0) {
            memberRepository.deleteAll();
        }
    }

    @Test
    @DisplayName("사용자 추가 및 조회")
    public void testUsers() {
        // 윤서준 사용자를 추가하고 아이디가 자동으로 생성되었는지 검증
        MemberRequest memberRequest = MemberRequest.builder().name("윤서준").age(10).build();
        MemberResponse memberResponse = memberService.create(memberRequest);
        assertThat(memberResponse.getId()).isNotNull();

        // 윤광철 사용자를 추가하고 아이디가 자동으로 생성되었는지 검증
        memberRequest = MemberRequest.builder().name("윤광철").age(43).build();
        memberResponse = memberService.create(memberRequest);
        assertThat(memberResponse.getId()).isNotNull();

        // "윤"이라는 글자가 들어간 사용자를 모두 조회하여 두명이 조회되는지 검증
        List<MemberResponse> results = memberService.findAll("윤");
        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("트랜잭션 커밋 테스트")
    public void testTransactionalCommit() {
        // 모두 네명의 사용자를 추가하며 데이터에 오류가 없기 때문에 트랜잭션에서 네개의 입력 모두 커밋되어야 한다.
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).build(),
                MemberRequest.builder().name("윤광철").email("KwangcheolYoon@hanbit.co.kr").age(43).build(),
                MemberRequest.builder().name("김도윤").email("DoyunKim@hanbit.co.kr").age(11).build(),
                MemberRequest.builder().name("공미영").email("MiyeongKong@hanbit.co.kr").age(28).build()
        );
        try {
            memberService.createList(memberRequests);
        } catch (Exception ignored) {}
        assertThat(memberRepository.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void testTransactionalRollback() {
        // 모두 네명의 사용자를 추가하지만 세번째 사용자는 첫번째 사용자와 이메일이 동일하므로 트랜잭션에서 롤백이 일어나야 한다.
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).build(),
                MemberRequest.builder().name("윤광철").email("KwangcheolYoon@hanbit.co.kr").age(43).build(),
                MemberRequest.builder().name("김도윤").email("SeojunYoon@hanbit.co.kr").age(11).build(),
                MemberRequest.builder().name("공미영").email("MiyeongKong@hanbit.co.kr").age(28).build()
        );
        try {
            memberService.createList(memberRequests);
        } catch (Exception ignored) {}
        assertThat(memberRepository.count()).isEqualTo(0);
    }

}

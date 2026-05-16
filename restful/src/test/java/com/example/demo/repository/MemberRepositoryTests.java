package com.example.demo.repository;

import com.example.demo.model.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("사용자 리파지토리 테스트")
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void doBeforeEach() {
        memberRepository.deleteAll();
        memberRepository.save(Member.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).enabled(true).build());
        memberRepository.save(Member.builder().name("윤광철").email("KwangcheolYoon@hanbit.co.kr").age(43).enabled(true).build());
        memberRepository.save(Member.builder().name("공미영").email("MiyeongKong@hanbit.co.kr").age(26).enabled(false).build());
        memberRepository.save(Member.builder().name("김도윤").email("DoyunKim@hanbit.co.kr").age(10).enabled(true).build());
    }

    @AfterEach
    public void doAfterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("조검 검색 테스트")
    public void testMemberCase1() {
        // 사용자 리파지토리에 저장된 개수가 4인지 검증
        assertThat(memberRepository.count()).isEqualTo(4);
        // '윤서준'이라는 이름으로 검색된 결과 개수가 1인지 검증
        assertThat(memberRepository.findByName("윤서준").size()).isEqualTo(1);
        // 이름이 '윤서준'이고 이메일이 'SeojunYoon@hanbit.co.kr'인 사용자를 조회한 결과 개수가 1인지 검증
        assertThat(memberRepository.findByNameAndEmail("윤서준", "SeojunYoon@hanbit.co.kr").size()).isEqualTo(1);
        // 이름이 '윤서준'이거나 또는 이메일이 'KwangcheolYoon@hanbit.co.kr'인 사용자를 조회한 결과 개수가 2인지 검증
        assertThat(memberRepository.findByNameOrEmail("윤서준", "KwangcheolYoon@hanbit.co.kr").size()).isEqualTo(2);
        // 이름에 '윤'이라는 글자가 포함된 사용자를 조회한 결과 개수가 3인지 검증
        assertThat(memberRepository.findByNameContaining("윤").size()).isEqualTo(3);
        // 이름이 '영'으로 끝나는 사람을 조회한 결과 개수가 1인지 검증
        assertThat(memberRepository.findByNameLike("%영").size()).isEqualTo(1);
        // 나이가 26를 초과하는 사람의 수가 1인지 검증
        assertThat(memberRepository.findByAgeGreaterThan(26).size()).isEqualTo(1);
        // 나이가 26세 이상인 사람의 수가 2인지 검증
        assertThat(memberRepository.findByAgeGreaterThanEqual(26).size()).isEqualTo(2);
        // 나이가 26세 미만인 사람의 수가 2인지 검증
        assertThat(memberRepository.findByAgeLessThan(26).size()).isEqualTo(2);
        // 나이가 26세 이하인 사람의 수가 3인지 검증
        assertThat(memberRepository.findByAgeLessThanEqual(26).size()).isEqualTo(3);
    }

    @RepeatedTest(value = 3, name="테스트 {displayName} 중 {currentRepetition} of {totalRepetitions}")
    @DisplayName("정렬 순서 테스트")
    public void testMemberCase2() {
        // 사용자 이름 순으로 조회를 한 결과 개수가 4인지 검증
        assertThat(memberRepository.findAllByOrderByNameAsc().size()).isEqualTo(4);
        // 사용자 이름 순으로 조회를 한 첫번째 사람의 이름이 '공미영'인지 검증
        assertThat(memberRepository.findAllByOrderByNameAsc().get(0).getName()).isEqualTo("공미영");
    }

    @Test
    @DisplayName("JPQL 테스트")
    //@Disabled("잠시 테스트 중단")
    public void testMemberCase3() {
        // 사용자가 활성화(enabled = true)되어 있고 이메일이 있는 사람의 수가 1인지 검증 - JPQL 사용
        assertThat(memberRepository.getActiveAdultWithEmail(true).size()).isEqualTo(1);
        // 사용자가 활성화(enabled = true)되어 있고 이메일이 있는 사람의 수가 1인지 검증 - Native SQL 사용
        assertThat(memberRepository.getActiveAdultWithEmailByNative(true).size()).isEqualTo(1);
    }
}

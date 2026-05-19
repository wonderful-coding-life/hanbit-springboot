
# 테스트 케이스 작성
- 메서드 이름만 봐도 어떤 테스트를 하려는지 알 수 있도록 명확하게 메서드 이름을 만든다.
- 하나의 테스트는 하나의 핵심만 검증
```java
@Test
void 회원을_저장한다() {}
@Test
void 회원을_조회한다() {}
@Test
void 회원을_수정한다() {}
@Test
void 회원을_삭제한다() {}
```
- 테스트끼리 순서에 의존하지 않기
```java
@Test
void step1_회원가입() {}

@Test
void step2_회원조회() {}
```
- given, when, then 구조 유지
```java
@Test
public void 회원을_생성하면_아이디가_부여된다() {
    // given
    var member = Member.builder().name("윤지웅").email("JiwoonYun@hanbit.co.kr").age(10).build();
    // when
    var saved = memberRepository.save(member);
    // then
    assertThat(saved.getId()).isNotNull();
}
```
- Spring 테스트에서 @Transactional을 붙이면 테스트 종료 후 롤백
```java
@Transactional
@Test
void 회원을_생성하면_아이디가_부여된다() {
    Member member = memberRepository.save(new Member("kim"));
    assertThat(member.getId()).isNotNull();
}
```

# JUnit
- @SpringBootTest
- @Test
- @RepeatedTest(value = 3, name="테스트 {displayName} 중 {currentRepetition} of {totalRepetitions}")
- @BeforeEach
- @AfterEach
- @DisplayName("...")
- @Disabled("잠시 테스트 중단")

import static org.assertj.core.api.Assertions.assertThat;
```java
assertThat(memberRepository.count()).isEqualTo(4);
```
- isEqualTo()
- isGreaterThan(), isLessThan()
- isGreaterThanOrEqualTo(), isLessThanOrEqualTo()
- isNegative(), isZero(), isPositive()
- isBetween()
- isOdd(), isEven()

# 통합테스트
```java
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {
    @Autowired
    private MockMvc mockMvc;
    
    // ...
}
```

```java
// prepare request builder
var requestBuilder = MockMvcRequestBuilders.post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestString);

// request to MockMvc and validate status code
// MockMvcResultMatchers.status(), content(), jsonPath()...
MvcResult mvcResult = mockMvc.perform(requestBuilder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("""
                {
                    "name": "윤서준",
                    "email": "SeojunYoon@hanbit.co.kr"
                }
                """, JsonCompareMode.LENIENT))
        .andExpect(jsonPath("$.id").isNumber())
        .andReturn();
```
# 단위테스트

## MockitoBean vs. MockitoSpyBean
@MockitoBean과 @MockitoSpyBean은 Spring Test에서 Bean을 Mockito 기반으로 대체할 때 사용하는 애노테이션

- @MockitoBean → 가짜 Mock 객체 생성
완전히 가짜(mock) 객체를 스프링 빈으로 등록합니다. 즉 실제 메서드는 실행되지 않습니다.
```java
@MockitoBean
MemberRepository memberRepository;

when(memberRepository.findById(1L))
        .thenReturn(Optional.of(member));

memberRepository.findById(1L)
```

- @MockitoSpyBean → 실제 객체를 감싼 Spy 객체 생성.
실제 스프링 빈을 기반으로 Spy 객체를 만듭니다.
즉 기본적으로는 실제 메서드가 실행됩니다.
다만 일부 메서드만 가로채서 변경할 수 있습니다. 즉, 일부만 모킹.
```java
@MockitoSpyBean
MemberService memberService;

doReturn("HELLO")
    .when(memberService)
    .getMessage();

memberService.findMember();
```

## 테스트 커버리지

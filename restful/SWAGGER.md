# OpenAPI Docs

SpringDoc OpenAPI는 Spring MVC Controller를 분석하여 OpenAPI 문서를 자동 생성하고, Swagger UI를 통해 브라우저에서 API 문서를 확인하고 테스트할 수 있게 해줍니다. SpringDoc은 애플리케이션 실행 시 Spring 설정, 클래스 구조, 애너테이션을 분석해 JSON/YAML/HTML 문서를 생성합니다. :contentReference[oaicite:0]{index=0}

## 1. Spring Initializr 의존성

Spring Initializr에서 다음 의존성을 선택합니다.

- Spring Web
- Lombok
- Validation (없어도 되지만 있다면 swagger 문서에 자동 반영된다.)
- SpringDoc OpenAPI

Spring Boot 4에서는 SpringDoc OpenAPI 3.x 계열을 사용합니다.

Gradle에는 보통 다음과 같은 의존성이 포함됩니다.

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui'
}
```

## 2. 기본 접속 경로

애플리케이션 실행 후 Swagger UI에 접속합니다.

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON 문서는 다음 주소에서 확인할 수 있습니다.

```text
http://localhost:8080/v3/api-docs
```

## 3. application.properties 기본 설정

```properties
spring.application.name=openapi-docs

# Swagger 기본 활성화
springdoc.swagger-ui.enabled=true

# OpenAPI Docs 기본 활성화
springdoc.api-docs.enabled=true

# Swagger UI 경로 변경
springdoc.swagger-ui.path=/swagger-ui.html

# OpenAPI JSON 경로
springdoc.api-docs.path=/v3/api-docs

# Try it out 기본 활성화
springdoc.swagger-ui.try-it-out-enabled=true

# API 정렬
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
```

설정 후 Swagger UI 접속 주소:

```text
http://localhost:8080/swagger-ui.html
```

## 4. OpenAPI 기본 정보 설정

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Member API")
                        .description("회원 관리 API 문서")
                        .version("v1.0.0"));
    }
}
```

## 5. 클래스에 설명 붙이기

Controller 클래스에는 `@Tag`를 사용합니다.

```java
@Tag(name = "회원 API", description = "회원 조회, 등록, 수정, 삭제 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
}
```

## 6. 메서드에 설명 붙이기

API 메서드에는 `@Operation`을 사용합니다.

```java
@Operation(
        summary = "회원 단건 조회",
        description = "회원 ID를 사용하여 회원 정보를 조회합니다."
)
@GetMapping("/{id}")
public MemberResponse get(@PathVariable("id") Long id) {
    return memberService.findById(id);
}
```

## 7. 파라미터에 설명 붙이기

경로 변수나 요청 파라미터에는 `@Parameter`를 사용합니다.

```java
@Operation(summary = "회원 단건 조회")
@GetMapping("/{id}")
public MemberResponse get(
        @Parameter(description = "회원 ID", example = "1")
        @PathVariable("id") Long id
) {
    return memberService.findById(id);
}
```

```java
@Operation(summary = "회원 검색")
@GetMapping
public List<MemberResponse> search(
        @Parameter(description = "회원 이름", example = "김철수")
        @RequestParam String name
) {
    return memberService.search(name);
}
```

복합 객체 형태의 요청 파라미터를 사용하는 경우에는 `@ParameterObject`를 사용할 수 있습니다.

예를 들어 Spring Data의 `Pageable` 객체를 사용하는 경우 Swagger UI에서 `page`, `size`, `sort` 파라미터를 개별적으로 표시할 수 있습니다.

```java
@Operation(summary = "게시글 페이지 조회")
@GetMapping
public Page<ArticleResponse> getAll(
        @ParameterObject
        @PageableDefault(
                page = 0,
                size = 10,
                sort = "id",
                direction = Sort.Direction.DESC
        )
        Pageable pageable
) {
    return articleService.findAll(pageable);
}
```

위와 같이 설정하면 Swagger UI에서 다음과 같은 요청 파라미터 입력 항목이 자동으로 생성됩니다.

- `page`
- `size`
- `sort`

예시 요청:

```http
GET /articles?page=0&size=10&sort=id,desc
```

`@ParameterObject`는 `Pageable` 뿐만 아니라 일반 DTO 객체에도 사용할 수 있습니다.

```java
public class MemberSearchCondition {

    private String name;
    private Integer age;

    // getter/setter
}
```

```java
@Operation(summary = "회원 조건 검색")
@GetMapping("/search")
public List<MemberResponse> search(
        @ParameterObject MemberSearchCondition condition
) {
    return memberService.search(condition);
}
```

요청 예시:

```http
GET /members/search?name=kim&age=20
```

이 경우 Swagger UI에서 `name`, `age` 입력 필드가 자동으로 생성됩니다.

## 8. RequestBody 설명 붙이기

Request DTO에는 `@Schema`를 사용합니다.

```java
@Schema(description = "회원 등록 요청")
public record MemberCreateRequest(

        @Schema(description = "회원 이름", example = "김철수")
        @NotBlank
        String name,

        @Schema(description = "이메일", example = "kim@example.com")
        @Email
        String email,

        @Schema(description = "나이", example = "20")
        @Min(0)
        Integer age
) {
}
```

Controller에서는 다음처럼 사용합니다.

```java
@Operation(summary = "회원 등록")
@PostMapping
public MemberResponse create(
        @RequestBody @Valid MemberCreateRequest request
) {
    return memberService.create(request);
}
```

## 9. Response DTO 설명 붙이기

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 생성 요청 DTO")
public class MemberRequest {

    @Schema(description = "회원 이름", example = "김철수")
    private String name;

    @Schema(description = "회원 이메일", example = "kim@example.com")
    private String email;

    @Schema(description = "회원 나이", example = "20")
    private Integer age;
}
```

## 10. 자주 사용하는 애노테이션

| 애노테이션              | 사용 위치          | 설명                                   |
| ------------------ | -------------- | ------------------------------------ |
| `@Tag`             | Controller 클래스 | API 그룹 설명                            |
| `@Operation`       | Controller 메서드 | API 기능 설명                            |
| `@Parameter`       | 메서드 파라미터       | PathVariable, RequestParam 설명        |
| `@ParameterObject` | 객체 파라미터        | Pageable, 검색 조건 DTO 등의 요청 파라미터 객체 설명 |
| `@Schema`          | DTO 클래스/필드     | 요청/응답 모델 설명                          |

## 11. 주의사항

`@PathVariable`은 경로 변수 이름을 명시하는 것이 안전합니다.

```java
@GetMapping("/{id}")
public MemberResponse get(@PathVariable("id") Long id) {
    return memberService.findById(id);
}
```

경로의 `{id}`와 `@PathVariable("id")` 이름이 일치해야 합니다.

## 12. 클라이언트 생성

메이븐 저장소에서 openapi-generator-cli.jar를 다운로드하면,
OpenAPI 문서를 기반으로 다양한 기술 스택의 클라이언트 코드를 자동 생성할 수 있다.

특히 React + TypeScript 환경에서는 API 호출 코드와 타입 정의를 자동으로 생성할 수 있어 실무에서도 많이 활용된다.

```shell
# Java WebClient 기반 클라이언트 생성
java -jar openapi-generator-cli-7.22.0.jar generate \
  -i http://localhost:8080/v3/api-docs \
  -g java \
  --library webclient \
  -o ./generated-client

# TypeScript Fetch 기반 클라이언트 생성
java -jar openapi-generator-cli-7.22.0.jar generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-fetch \
  -o ./ts-client
```
생성된 프로젝트에는 다음과 같은 코드들이 포함된다.

- API 호출 함수
- Request/Response DTO 타입
- HTTP Client 설정 코드
- 직렬화/역직렬화 코드

이를 통해 프론트엔드와 백엔드 간의 타입 불일치를 줄이고, API 변경 사항을 보다 안정적으로 반영할 수 있다.

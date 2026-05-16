

# 스테레오 타입 애노테이션

- 특정 역할을 가진 Bean임을 나타내기 위해 사용하는 애노테이션이다.
- stereotype의 어원은 인쇄 분야에서 사용되던 “고정된 인쇄판(stereotype plate)”에서 유래했다.
- 즉, 매번 활자를 조합하는 것이 아니라 미리 정해진 형태와 역할을 가진 틀이라는 의미를 가진다.
- Spring에서는 클래스의 역할을 구분하기 위해 다음과 같은 스테레오 타입 애노테이션을 제공한다.
    - @Controller
    - @RestController
    - @Service
    - @Repository
- MyBatis에서는 Mapper 인터페이스를 Bean으로 등록하기 위해 @Mapper를 사용하기도 한다.

# 주요 애노테이션

- @RestController: @Controller + @ResponseBody
- @GetMapping("/{id}")
- @PutMapping("/{id}")
- @DeleteMapping("/{id}")
- @PatchMapping("/{id}")
- @PathVariable Long id
- @RequestParam int page, @RequestParam("page") : Query String 값을 받는다.
- @RequestBody MemberCreateRequest request : HTTP Body(JSON)를 객체로 변환한다.
- @RequestHeader("Authorization") String token : HTTP Header 값을 받는다.
- @CookieValue("SESSION") String sessionId : 쿠키 값을 조회한다.
- @ResponseStatus(HttpStatus.CREATED) : 특정 HTTP 상태 코드를 지정한다.
- @RequestPart MultipartFile file : multipart/form-data 요청 처리, 파일 + JSON 동시 처리 가능

# 응답
- @ResponseBody 또는 @RestController에서 객체를 반환하면 JSON으로 변환되어 HTTP Response Body로 전달된다.
- 클라이언트의 요청 헤더가 Accept : application/xml라면 XML 형식으로 전달. 이 경우 Jackson XML 등의 추가 의존성이 필요
```groovy
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'
```
- ResponseEntity를 사용하면 HTTP 상태 코드(Status), 헤더(Header), Body를 함께 제어할 수 있다.
- 쿠키는 HTTP Header(Set-Cookie)를 통해 전달된다.
```java
ResponseEntity<Member> response = ResponseEntity.ok(member); // status(), body()
```

# 예외 처리
- @ExceptionHandler를 사용하여 예외를 처리할 수 있다.
- 일반적으로 ex.getMessage()와 같은 내부 예외 메시지를 그대로 사용자에게 노출하기 보다는, 보안상 개략적인 메시지만 제공하는 것이 좋다.
- @ExceptionHandler는 개별 컨트롤러 내부에 정의할 수도 있고, @ControllerAdvice 또는 @RestControllerAdvice를 사용하여 전역(Global)으로 처리할 수도 있다.
- @Controller 또는 @ControllerAdvice에서 반환값이 String인 경우에는 View 이름으로 인식된다.
- 반면 @RestController 또는 @RestControllerAdvice에서는 반환값이 HTTP Response Body로 처리된다.
```java
@ExceptionHandler(SQLException.class)
public ResponseEntity<ExceptionDetails> handleException(SQLException ex, HttpServletRequest request) {
    return ResponseEntity.status(500).body(ExceptionDetails.builder()
            .timestamp(new Date())
            .status(500)
            .path(getPath(request))
            .reason("데이터베이스에 문제가 발생했습니다.").build());
}
```

---

# RESTful API 호출
- WebClient : 비동기/리액티브 필요, 직접 build.gradle에 추가, Spring Reactive Web (WebFlux)이 필요
- RestClient : 일반적인 REST 호출, 직접 build.gradle에 추가, Spring Web MVC 필요
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webclient'
    implementation 'org.springframework.boot:spring-boot-starter-restclient'
}
```

# RestTemplate
- @Bean RestTemplate
- restTemplate.postForEntity, getForEntity, put, patchForObject, delete
- Spring Boot Starter WebFlux
- @Bean WebClient
- webClient.get(), post()

# 그래들 테스트 후 결과 확인 (gradle test)
- 명령프롬프트 : echo %ERRORLEVEL%
- 파웨쉘 : Write-Host "Exit Code: $LASTEXITCODE"
- 리눅스/맥OS : echo $?
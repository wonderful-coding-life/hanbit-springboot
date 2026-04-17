
### Step 0 - 준비
- 모델 엔티티 정의
- 리파지토리 정의 - 이메일 검색 메서드 추가
- 컨트롤러 및 템플릿 작성 - 홈, 상품목록, 상품주문, 회원관리
- 실행하면 기본적으로 모든 접근은 로그인한 사용자만 가능 (user, random password)

### Step 1 - 사용자 인증
- 사용자 초기화 - 일반 회원(권한이 특별히 없더라도 ROLE_USER와 같은 기본 권한 설정 권장), 관리자 회원(ROLE_ADMIN)
- 스프링 시큐리티 설정 - PasswordEncoder, 사용자 인증 구현

### Step 2 - 권한 인가
- 시큐리티 필터 체인 생성

### Step 3 - 로그인, 로그아웃 폼 커스텀
- 시큐리티 필터 체인 생성시 loginForm, logout 설정

### Step 4 - 뷰에서 인증, 인가 정보 사용하기
- thymeleaf-extras-springsecurity6

### Step 5 - 컨트롤러에서 인증, 인가 정보 사용하기
- @AuthenticationPrincipal UserDetails userDetails

### Step 5 - 스프링시큐리티 우회
- 정적 리소스 및 H2 Console을 굳이 스프링 시큐리티를 통과할 필요가 없다

---

### Spring Security Filter Chain
Security Filter Chain을 거쳐야 Controller에 도달할 수 있음.
Application에서 HttpSecurity 객체를 사용하여 우리가 원하는 Security Filter Chain을 만들어 Bean으로 등록함.
#### (1) 인증 준비
- SecurityContextHolderFilter - 저장소(주로 세션)에 있는 기존 인증 정보를 현재 요청에 로드하는 필터이고,
  JWT나 HTTP Basic처럼 상태를 저장하지 않는 방식에서는 주로 뒤의 인증 필터가 매 요청마다 인증을 수행
- CsrfFilter
- CorsFilter
- LogoutFilter
#### (2) 인증 (Authentication)
우리가 원하는 인증 필터를 설정할 수 있으며, 각 인증 필터는 사용자 정보(Authentication)를 입력으로 부터 가져와
AuthenticationManager → AuthenticationProvider(FormLogin과 HttpBasic의 경우 DaoAuthenticationProvider -> UserDetailsService)를 통해 인증을 하게 됨.
```
http.formLogin(); → UsernamePasswordAuthenticationFilter
http.httpBasic(); → BasicAuthenticationFilter
http.oauth2ResourceServer().jwt(); → BearerTokenAuthenticationFilter
```
- UsernamePasswordAuthenticationFilter 폼 로그인 (/login)
- BearerTokenAuthenticationFilter JWT 토큰 인증 (Authorization: Bearer 헤더 처리)
- BasicAuthenticationFilter HTTP Basic 인증 처리
- AnonymousAuthenticationFilter (인증되지 않은 요청에 대해 “익명 사용자”를 만들어 넣어주는 필터)
#### (3) 예외 처리
- ExceptionTranslationFilter (ExceptionTranslationFilter는 예외를 직접 처리하지 않고, 상황에 따라 EntryPoint 또는 AccessDeniedHandler로 위임한다)
  → 인증이 안 된 경우 → AuthenticationEntryPoint 호출 (설정에 따라 로그인 페이지로 redirect, 401 Unauthorized, 특정 인증 방식 시작(Basic, OAuth2등))
  → 인증은 되었지만 권한이 없는 경우 → AccessDeniedHandler 호출 (기본 동작 403 Forbidden 반환)
#### (4) 인가 (Authorization)
- AuthorizationFilter 최종 권한 체크 (`hasRole`, `permitAll` 등 규칙 평가)



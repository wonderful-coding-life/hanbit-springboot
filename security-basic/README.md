
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

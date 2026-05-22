## 웹 페이지 스타일링 프롬프트 예

```text
Spring Boot + Thymeleaf로 만든 웹 애플리케이션 전체 화면의 UI를 개선하고 싶습니다.

목표:
- 전체 화면을 일관된 modern SaaS / clean admin dashboard 스타일로 개선
- HTML은 개발자가 계속 수정하기 쉬운 구조 유지
- 인라인 style 사용 금지
- CSS 프레임워크는 Bootstrap 5를 기준으로 사용하고 커스텀 스타일은 /static/css/app.css로 분리
- Thymeleaf 문법(th:*, layout, fragment 등)은 유지
- 기존 form action, name, id, th:field, th:each, th:if 등 기능 로직은 변경하지 않기
- 화면의 구조는 크게 바꾸지 말고, UI/UX와 레이아웃만 개선
- 로그인, 목록, 상세, 등록/수정 폼, 에러 페이지까지 전체적으로 톤앤매너 통일
- 반응형 지원
- 접근성 고려
- 과한 애니메이션은 제외

작업 방식:
1. templates 폴더의 HTML 파일들을 분석
2. 공통 레이아웃, 버튼, 입력 폼, 테이블, 카드, 알림 메시지 스타일을 정의
3. 중복 스타일은 공통 CSS 클래스로 분리
4. HTML에는 의미 있는 class만 추가
5. CSS는 별도 파일로 작성
6. 각 HTML 파일에서 CSS를 링크하도록 수정

산출물:
- 수정된 HTML 파일들
- /static/css/app.css
- 필요하다면 화면별 CSS 파일
- 변경한 주요 CSS 클래스 설명을 STYLE_GUIDE.md 파일로 작성

디자인 방향:
- modern SaaS UI
- clean admin dashboard
- subtle shadow
- rounded corners
- spacious layout
- simple typography
- mobile responsive

중요:
HTML 구조와 Thymeleaf 기능 코드는 최대한 유지하고,
디자인을 위해 필요한 class 추가만 허용합니다.
style 속성은 사용하지 말고 모든 스타일은 CSS 파일에 작성해 주세요.
```


# 추가 화면들에 대해 기존 UI 스타일 적용하는 프롬프트 예

```
현재 Spring Boot + Thymeleaf 프로젝트에는
이미 적용된 공통 UI 스타일(app.css)과
STYLE_GUIDE.md 기반의 디자인 시스템이 존재합니다.

새로 추가된 HTML 화면들을
기존 프로젝트의 UI 스타일과 동일한 톤앤매너로 맞춰 주세요.

목표:
- 기존 디자인 시스템 유지
- 기존 app.css 및 STYLE_GUIDE.md 적극 활용
- 기존 공통 CSS 클래스 최대한 재사용
- 새로운 CSS 추가는 최소화
- 화면 전체의 일관된 modern SaaS / clean admin dashboard 스타일 유지
- HTML은 개발자가 계속 수정하기 쉬운 구조 유지
- 반응형 지원
- 접근성 고려
- 과한 애니메이션 제외

중요:
새로운 디자인을 만들기보다,
기존 프로젝트의 UI 시스템에 자연스럽게 통합하는 것이 목표입니다.

반드시 지켜야 할 사항:
- Thymeleaf 문법(th:*, layout, fragment 등) 유지
- 기존 form action, name, id, th:field, th:each, th:if 등 기능 로직 변경 금지
- HTML 구조는 크게 변경하지 말고 UI/UX 중심으로만 개선
- 인라인 style 사용 금지
- 모든 스타일은 CSS 파일로 관리
- 의미 있는 class만 추가
- 기존 CSS naming convention 유지
- 기존 layout/fragment 구조 유지
- 기존 버튼/폼/테이블/카드 스타일과 동일한 패턴 유지

작업 방식:
1. 기존 templates 폴더의 HTML 파일 분석
2. 기존 app.css 분석
3. STYLE_GUIDE.md 분석
4. 현재 사용 중인 공통 UI 패턴 파악
5. 새 HTML 파일에 기존 공통 클래스 우선 적용
6. 필요한 경우에만 최소한의 CSS 추가
7. 중복 스타일 생성 금지
8. 기존 디자인 시스템과 일관성 유지

디자인 방향:
- modern SaaS UI
- clean admin dashboard
- subtle shadow
- rounded corners
- spacious layout
- simple typography
- mobile responsive

CSS 작성 규칙:
- Layout / Form / Table / Button / Alert / Card 등 영역별로 주석 구분
- 주요 클래스는 역할이 드러나는 이름 사용
- 재사용 가능한 공통 클래스 우선 작성
- style 속성 사용 금지

산출물:
- 수정된 HTML 파일들
- 추가 또는 수정된 CSS
- 필요한 경우 화면별 CSS 파일
- 새로 추가된 주요 CSS 클래스 설명

추가 규칙:
STYLE_GUIDE.md에 없는 새로운 UI 패턴이 필요하다면,
기존 naming convention과 디자인 방향에 맞춰 최소한으로 추가해 주세요.
```
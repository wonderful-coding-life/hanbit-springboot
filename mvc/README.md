# Spring MVC + Thymeleaf
- /member/add
- /member/edit?id=1
- /member/list
- /member/delete?id=1

# Bootstrap + Pagination
- /article/list

# Error page
- Spring Boot에서 요청 처리 중 에러가 발생하면 /error로 포워딩
- 우리가 직접 에러 컨트롤러 /error를 구현하지 않았다면 BasicErrorController가 처리하여 뷰를 다음과 같은 우선 순위로 탐색 
```
/error/404.html  (있으면 사용)
/error/500.html  (있으면 사용)
/error.html      (fallback)
Whitelabel Error Page (없으면 기본 페이지)
```
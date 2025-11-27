### Spring Session for JDBC
- 내장 데이터베이스의 경우 스키마 자동 생성
- 외부 데이터베이스의 경우 org.springframework.session.jdbc에 각 DB별 스키마 참고

### Spring Boot Actuator
- 포함하면 기본적으로 /actuator/health 활성화
- 나머지 활성화 하려면 다음과 같이 설정, /actuator/loggers, /actuator/beans, /actuator/mappings, /actuator/scheduledtasks
```properties
management.endpoints.web.exposure.include=*
```

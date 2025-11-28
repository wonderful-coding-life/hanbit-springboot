## Spring Boot 4.0.0 Migration
- Spring Boot 4는 Spring Framework 7 기반
- 최소 JDK 17 이상, JDK 21 이상 권장
- gradle version 최신 버전(최소 8.14 이상)으로 변경 (gradle-wrapper.properties)
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.2.1-bin.zip
```
- build.gradle에서 Spring Boot 버전 변경
- 부트 스타터 패키지 이름 변경 spring-boot-starter-web(deprecated) -> spring-boot-starter-webmvc 등
- 테스트 패키지도 개별 부트 스타터 포함 (예전에는 spring-boot-starter-test 하나만 포함하면 되었고 지금도 사용할 수 있으나 개별 패키지 포함하는 것을 추천)
```build.gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '4.0.0'
	id 'io.spring.dependency-management' version '1.1.7'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-session-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-session-jdbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```
- H2 Database가 업데이트 되면서 좀 더 엄격하게 문법 체크. schema.sql에서 DATETIME -> TIMESTAMP으로 변경
```sql
CREATE TABLE IF NOT EXISTS article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(4096),
    created TIMESTAMP,
    updated TIMESTAMP,
    member_id BIGINT,
    FOREIGN KEY(member_id) REFERENCES member(id)
);
```
- Spring Security Filter Chain은 반드시 /로 시작하는 패턴이어야 함. article/list -> /article/list
```java
http
        .csrf(withDefaults())
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/article/list", "/article/content").permitAll()
                .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/signup").permitAll()
                .requestMatchers("/health").permitAll()
                .anyRequest().authenticated())
```
- Jackson2 --> Jackson3로 변경되어 다음과 같이 ObjectMapper, TypeReference 패키지가 바뀌었다.
```java
// Jackson2
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
// Jackson3
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;
```
- Jackson3에서는 ObjectMapper를 사용하여 JSON -> Java Object 만들때 반드시 기본 생성자가 있어야 한다. @NoArgsConstructor 사용.
```java
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    // ...
}
```
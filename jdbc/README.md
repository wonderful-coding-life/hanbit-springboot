# 프로젝트 셋업

## MySQL 실습 데이터베이스
- Server: xxx.xxx.xxx.xxx:3306
- Database: tutor, edu1, edu2, ...
- Account: tutor/tutorp, edu1/edu1p, edu2/edu3p, ...

## 테이블 생성
```sql
CREATE TABLE IF NOT EXISTS member (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	email VARCHAR(256) NOT NULL UNIQUE,
	age INTEGER
);
```

## 데이터 입력
```sql
INSERT INTO member(name, email, age) VALUES('윤서준', 'SeojunYoon@hanbit.co.kr', 10);
INSERT INTO member(name, email, age) VALUES('윤광철', 'KwangcheolYoon@hanbit.co.kr', 43);
INSERT INTO member(name, email, age) VALUES('공미영', 'MiyeongKong@hanbit.co.kr', 23);
INSERT INTO member(name, email, age) VALUES('김도윤', 'DoyunKim@hanbit.co.kr', 10);
```

## 데이터 조회
```sql
SELECT * FROM member;
```

## PreparedStatement
```java
PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE id=?");
preparedStatement.setLong(1, 2L);
```

## IntelliJ 터미널에서 System.out.println()으로 출력한 한글이 깨지는 경우
- 실행 옵션 > 편집
- 옵션 수정 > VM 옵션 추가
- 옵션 추가 -Dsun.stdout.encoding=COMPAT

## 롬복추가
```
compileOnly 'org.projectlombok:lombok:1.18.46'
annotationProcessor 'org.projectlombok:lombok:1.18.46'
```

## JUnit Platform Launcher

JUnit 테스트를 실행할 때는 테스트 작성용 API뿐 아니라 테스트 실행을 위한 런처가 필요하다.

```
dependencies {
    testImplementation platform('org.junit:junit-bom:6.1.0')
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}
```

`junit-jupiter`는 `@Test`, `Assertions` 등 JUnit Jupiter 기반 테스트 작성과 실행에 필요한 기본 구성을 제공한다.

`junit-platform-launcher`는 Gradle이나 IDE가 JUnit Platform 기반 테스트를 실행할 때 사용하는 런처이다. 테스트 코드에서 직접 사용하는 클래스는 아니므로 보통 `testRuntimeOnly`로 추가한다.

IntelliJ에서 새 Gradle 프로젝트를 만들면 최소 구성만 생성되기 때문에 `junit-platform-launcher`가 생략될 수 있다. 간단한 테스트는 동작할 수 있지만, Gradle과 IDE에서 테스트 실행 환경을 안정적으로 맞추려면 명시적으로 추가하는 것이 좋다.

## Slf4J, Logback 추가
```
implementation 'org.slf4j:slf4j-api:2.0.18'
implementation 'ch.qos.logback:logback-classic:1.5.34'
```

# 코드 설명

## JDBC 리소스 정리

JDBC에서 사용하는 `Connection`, `Statement`, `PreparedStatement`, `ResultSet`은 사용 후 반드시 `close()` 해야 하는 리소스이다.

직접 `close()`를 호출할 수도 있지만, 예외가 발생하면 리소스가 닫히지 않을 수 있으므로 `try-with-resources` 문법을 사용하는 것이 안전하다.

```java
try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    preparedStatement.setLong(1, id);

    try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
            // 조회 결과 처리
        }
    }
}
```

`try (...)` 안에 선언한 리소스는 `try` 블록이 정상 종료되거나 예외가 발생하더라도 자동으로 `close()` 된다.

여러 리소스를 사용하는 경우 닫히는 순서는 생성된 순서의 반대이다.

```text
ResultSet close
PreparedStatement close
```

단, 메서드 밖에서 전달받은 `Connection`은 해당 메서드에서 생성한 리소스가 아니므로 보통 그 메서드 안에서 닫지 않는다.

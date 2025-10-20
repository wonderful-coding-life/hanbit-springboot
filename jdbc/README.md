
### MySQL 실습 데이터베이스
- Server: xxx.xxx.xxx.xxx:3306
- Database: tutor, edu1, edu2, ...
- Account: tutor/tutorp, edu1/edu1p, edu2/edu3p, ...

### 테이블 생성
```sql
CREATE TABLE IF NOT EXISTS member (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	email VARCHAR(256) NOT NULL UNIQUE,
	age INTEGER
);
```

### 데이터 입력
```sql
INSERT INTO member(name, email, age) VALUES('윤서준', 'SeojunYoon@hanbit.co.kr', 10);
INSERT INTO member(name, email, age) VALUES('윤광철', 'KwangcheolYoon@hanbit.co.kr', 43);
INSERT INTO member(name, email, age) VALUES('공미영', 'MiyeongKong@hanbit.co.kr', 23);
INSERT INTO member(name, email, age) VALUES('김도윤', 'DoyunKim@hanbit.co.kr', 10);
```

### 데이터 조회
```sql
SELECT * FROM member;
```

### PreparedStatement
```java
PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE id=?");
preparedStatement.setLong(1, 2L);
```

### IntelliJ 터미널에서 System.out.println()으로 출력한 한글이 깨지는 경우
- 실행 옵션 > 편집
- 옵션 수정 > VM 옵션 추가
- 옵션 추가 -Dsun.stdout.encoding=COMPAT

## 롬복추가
```
    compileOnly 'org.projectlombok:lombok:1.18.42'
    annotationProcessor 'org.projectlombok:lombok:1.18.42'
```

## Slf4J, Logback 추가
```
    implementation 'org.slf4j:slf4j-api:2.0.17'
    implementation 'ch.qos.logback:logback-classic:1.5.19'
```
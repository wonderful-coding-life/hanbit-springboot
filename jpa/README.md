
```sql
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    age INTEGER
);

CREATE TABLE article (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(256),
   description VARCHAR(4096),
   created DATETIME,
   updated DATETIME,
   member_id BIGINT,
   FOREIGN KEY(member_id) REFERENCES member(id) ON DELETE CASCADE
);
```

# 운영 시스템을 위한 스키마 정의

개발 환경에서는 H2 데이터베이스를 사용하거나,
`ddl-auto=create`, `create-drop` 등의 설정을 통해
애플리케이션 실행 시 자동으로 스키마를 생성하며 개발할 수 있다.

하지만 운영 환경에서는 애플리케이션이 직접 데이터베이스 스키마를 생성하거나 수정하지 않도록 하는 것이 일반적이다.

만약 운영 환경에서 초기 스키마 생성이 필요하다면 다음과 같은 방법을 사용할 수 있다.

* DBA 또는 개발자가 직접 SQL 작성
* Hibernate의 DDL Export 기능을 사용하여 초기 DDL 생성

다음과 같이 설정하고 애플리케이션을 실행하면,
실제 데이터베이스에는 아무 작업도 수행하지 않고 DDL 파일만 생성할 수 있다.

```properties
# 운영 환경용 DDL 생성 설정

# 앱 실행 시 실제 DB에 대해 CREATE / ALTER / DROP 등을 수행하지 않음
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.jakarta.persistence.schema-generation.database.action=none

# Entity 기반 DDL 생성 요청
spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action=create

# 생성될 DDL 파일 위치
spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target=create.sql
```

앱 실행 후 프로젝트 실행 디렉토리 기준으로 `create.sql` 파일이 생성되며,
Hibernate가 Entity를 분석하여 생성한 DDL이 기록된다.

예시:

```sql
create table member (
    id bigint not null auto_increment,
    name varchar(255),
    primary key (id)
);
```

생성된 DDL은 운영 환경에 바로 적용하기보다는,
검토 및 수정 후 Flyway migration 파일(`V1__init.sql`) 등의 형태로 관리하는 것이 일반적이다.


# Flyway

* Spring Initializr에서 Flyway Migration을 포함하면,
  애플리케이션 실행 시 `resources/db/migration` 폴더에 있는 migration 스크립트를 확인하여
  현재 데이터베이스 기준 최신 버전까지 순차적으로 migration을 수행한다.

* Migration 파일 이름 형식은 다음과 같다.

```text
V버전__설명.sql
```

예시:

```text
V1__create_member.sql
V2__add_member_phone.sql
V3__create_article.sql
```

* Flyway는 데이터베이스에 `flyway_schema_history` 테이블을 생성하고,
  어떤 migration이 어디까지 적용되었는지 기록 및 관리한다.

* 최초 스키마 생성용 migration(`V1__...`)은 보통 Hibernate가 생성한 DDL을 참고하여 작성한다.

* 이후 migration 스크립트는 일반적으로 개발자가 직접 작성한다.

    * 스키마 변경 (`CREATE`, `ALTER`, `DROP`)
    * 데이터 변경 (`INSERT`, `UPDATE`, `DELETE`)

* 일부 자동 생성 또는 diff 기반 도움을 주는 도구들이 존재하지만,
  운영 환경에서는 migration SQL을 반드시 사람이 직접 검토하는 것이 일반적이다.

* 실무에서는 보통 다음과 같이 역할을 분리한다.

| 역할                   | 담당                              |
| -------------------- | ------------------------------- |
| Entity 기반 객체 매핑      | JPA / Hibernate                 |
| DDL 초안 생성            | Hibernate                       |
| DB 버전 및 migration 관리 | Flyway                          |
| 운영 DB 변경 적용          | Flyway                          |
| Entity ↔ DB 구조 검증    | Hibernate (`ddl-auto=validate`) |

* 일반적으로 운영 환경에서는 다음 설정을 사용한다.

```properties
spring.jpa.hibernate.ddl-auto=validate
```

즉, 실제 스키마 변경은 Flyway가 수행하고,
Hibernate는 Entity와 DB 구조가 일치하는지만 검증한다.

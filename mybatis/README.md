- LocalDate는 날짜만 저장할 때 사용한다.
- LocalTime은 시간만 저장할 때 사용한다.
- LocalDateTime은 타임존이 없는 "로컬 날짜/시간"이 의미인 경우에만 사용한다.
- 생성 시각, 수정 시각, 결제 시각, 로그 시각처럼 절대 시점을 저장해야 하는 경우에는 Instant를 사용한다.

| Java Type        | 의미                          | MySQL        | H2         | Oracle                      | PostgreSQL                | SQL Server        |
|------------------|-------------------------------|--------------|------------|-----------------------------|---------------------------|-------------------|
| LocalDate        | 날짜만                        | DATE         | DATE       | DATE                        | DATE                      | DATE              |
| LocalTime        | 시간만                        | TIME         | TIME       | ❌ (단독 TIME 타입 없음)     | TIME                      | TIME              |
| LocalDateTime    | 타임존 없음 (로컬 날짜/시간)  | DATETIME     | TIMESTAMP  | TIMESTAMP                   | TIMESTAMP                 | DATETIME2         |
| Instant          | 절대 시각 (UTC 기준)          | TIMESTAMP    | TIMESTAMP  | TIMESTAMP WITH TIME ZONE    | TIMESTAMP WITH TIME ZONE  | DATETIMEOFFSET    |
| ZonedDateTime    | 타임존 포함 날짜/시간         | ❌ (직접 대응 약함) | TIMESTAMP  | TIMESTAMP WITH TIME ZONE    | TIMESTAMP WITH TIME ZONE  | DATETIMEOFFSET    |

- 글로벌 서비스를 만들 때에는 절대 시점을 Instant로 UTC 기준 저장하고,
  각 앱에서는 사용자/국가의 시간대에 맞게 ZonedDateTime 등으로 변환해서 보여주는 것이 정석이다.
```java
Instant instant = Instant.now();
ZonedDateTime zdt = instant.atZone(ZoneId.of("Asia/Seoul"));
```
```html
<span th:text="${#temporals.format(zdt, 'yyyy-MM-dd HH:mm')}"></span>
```
| Java Type        | 의미                          | MySQL        | H2         | Oracle                      | PostgreSQL              | SQL Server        |
|------------------|-------------------------------|--------------|------------|-----------------------------|-------------------------|-------------------|
| LocalDate        | 날짜만                        | DATE         | DATE       | DATE                        | DATE                    | DATE              |
| LocalTime        | 시간만                        | TIME         | TIME       | ❌ (DATE 사용)               | TIME                    | TIME              |
| LocalDateTime    | 타임존 없음 (로컬 날짜/시간)  | DATETIME     | TIMESTAMP  | TIMESTAMP                   | TIMESTAMP               | DATETIME2         |
| Instant          | 절대 시각 (UTC 기준)          | TIMESTAMP    | TIMESTAMP  | TIMESTAMP WITH TIME ZONE    | TIMESTAMP WITH TIME ZONE| DATETIMEOFFSET    |

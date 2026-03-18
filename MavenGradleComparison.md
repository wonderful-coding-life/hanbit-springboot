# Maven vs Gradle 자주 사용하는 태스크 비교
| 목적 | Maven 명령어 | Gradle 명령어 | 설명 |
|------|-------------|--------------|------|
| 애플리케이션 실행 | `.\mvnw spring-boot:run` | `./gradlew bootRun` | Spring Boot 애플리케이션 실행 |
| 테스트 실행 | `.\mvnw test` | `./gradlew test` | 단위 테스트 실행 |
| 실행 가능한 JAR 생성 | `.\mvnw package` | `./gradlew bootJar` | 실행 가능한 fat jar 생성 |
| 빌드 결과 | `.\target\demo-0.0.1-SNAPSHOT.jar` | `./build/libs/demo-0.0.1-SNAPSHOT.jar` | 생성된 jar 위치 |
| 클린 + 빌드 | `.\mvnw clean package` | `./gradlew clean build` | 이전 빌드 삭제 후 전체 빌드 |
| 의존성 트리 확인 | `.\mvn dependency:tree` | `./gradlew dependencies` | 프로젝트 의존성 구조 확인 |
| 특정 의존성 분석 | `.\mvn dependency:tree -Dincludes=log4j` | `./gradlew dependencyInsight --dependency log4j` | 특정 라이브러리 의존성 추적 |
| 전체 빌드 (테스트 포함) | `.\mvnw package` | `./gradlew build` | compile + test + jar 생성 |
| Docker 이미지 생성 | `.\mvnw spring-boot:build-image` | `./gradlew bootBuildImage` | Buildpack 기반 컨테이너 이미지 생성 |
| 애플리케이션 실행 (profile 지정) | `.\mvnw spring-boot:run -Dspring-boot.run.profiles=dev` | `./gradlew bootRun --args='--spring.profiles.active=dev'` | 특정 profile로 실행 |


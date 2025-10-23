
## 문맥을 이해하며 답변을 생성하도록 하기
- SystemMessage, UserMessage, AssistantMessage를 순서대로 전달하면 됨 -> testChatMemory() in test classes
- 대화를 하다 보면 주제가 바뀌기도 하는데다 모든 ChatHistory를 전달하면 비용이 많이 들 수 있으므로 최근 메시지들만 10개 또는 20개 정도 채팅 윈도우 구간을 정해서 ChatMemory를 전달하는 것이 일반적
- 직접 대화를 데이터베이스에 저장 관리하지 않고 간편하게 ChatMemory를 사용할 수 있음 (Spring AI는 디폴트로 InMemoryChatMemoryRepository 사용) -> Controller

## Spring Initializer
- Spring Web
- OpenAI
- Lombok

## ChatMemoryRepository
- 기본적으로 InMemoryChatMemoryRepository 사용 (ConcurrentHashMap)
- JdbcChatMemoryRepository도 지원
```declarative
spring-ai-starter-model-chat-memory-repository-jdbc
mysql
```
- spring.ai.chat.memory.repository.jdbc.initialize-schema=always // 스키마 자동 생성
- org/springframework/ai/chat/memory/repository/jdbc/schema-@@platform@@.sql 참고
- 테스트를 해 보니 timestamp가 override되는 문제가 있어 보임. 윈도우 넘어가면 순서가 꼬여 제대로 된 답변을 생성할 수 없음.

## 스키마 참고
- org.springframework.ai:spring-ai-model-chat-memory-repository-jdbc
- schema-hsqldb.sql
- schema-mariadb.sql
- schema-mysql.sql
- schema-postgresql.sql
- schema-sqlserver.sql
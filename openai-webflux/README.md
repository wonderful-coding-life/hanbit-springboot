
## AI 모델의 응답을 모두 전달 받은 후에 한꺼번에 클라이언트로 반환하지 않고 조금씩 스트리밍
- 스프링부트 애플리케이션에서는 웹플럭스를 사용하여 SSE(Server-Sent Events)로 스트리밍
## SSE(Server-Sent Events)란?
- HTTP 기반의 단방향 스트리밍 프로토콜
- 표준 MIME 타입: text/event-stream (응답 헤더 Content-Type)
- 통신은 일반 HTTP 연결 위에서 이루어지며 계속 오픈된 상태 유지
- 주로 실시간 로그, 채팅 응답 스트림, AI 토큰 출력, 알림 시스템 등에 사용
- 스프링부트 웹 플럭스 사용 예 (컨트롤러 예: ChatController, 라우터 방식도 가능: ChatRouter, ChatHandler)
```declarative
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getChatResponse(@RequestParam("message") String message) {
        log.info("Chat message: {}", message);
        return chatModel.stream(message);
    }
```
- data:로 시작하는 텍스트 블록을 빈 줄로 구분해 실시간으로 전송
```declarative
data: 안녕

data: 나는 AI야

data: 반가워!
```
- Java Script의 SSE 스트림 수신 전용 객체 EventSource 사용 예
```declarative
const es = new EventSource("/chat?message=XXXX");
es.onopen = () => console.log("연결됨");
es.onmessage = (e) => console.log("수신:", e.data);
es.onerror = (e) => console.error("종료 또는 에러:", e);
```
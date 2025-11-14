
### application.properties
```properties
spring.application.name=demo

app.scheduler.order-fulfillment.enabled=true
app.scheduler.order-fulfillment.cron=0 0 0,12 * * *

app.scheduler.cancel-unpaid-orders.enabled=true
app.scheduler.cancel-unpaid-orders.cron=0 0 */1 * * *

app.scheduler.request-post-purchase-review.enabled=false
app.scheduler.request-post-purchase-review.cron=0 0 10 * * *
```
### yaml(야물) 구조화된 app 설정
- 복잡한 설정을 트리 형태로 작성하기 때문에 가독성이 좋다
- 문자열 리터럴로 "..."를 사용할 수 있어 특수문자나 공백을 안전하게 표시할 수 있다
- 스프링부트의 @ConfigurationProperties와 잘 맞는다
```yaml
app:
  scheduler:
    order-fulfillment:
      enabled: true
      cron: "0 0 0,12 * * *"   # 매일 0시, 12시
    cancel-unpaid-orders:
      enabled: true
      cron: "0 0 */1 * * *"    # 매시간 정각
    request-post-purchase-review:
      enabled: false           # 기본은 끔
      cron: "0 0 10 * * *"     # 매일 오전 10시
```
### 필요한 곳에서 직접 가져다 사용하는 방법
- Scheduler
- @EnableScheduling
- @ConditionalOnProperty
- @Scheduled
- @Value (not in lombok but in springframework)
```java
@Value("${app.scheduler.order-fulfillment.enabled}")
private boolean enabled;
```
### AppProperties와 같은 객체를 만들어서 의존성 주입 받아 사용하는 방법
- @ConfigurationProperties
 

# 익명 클래스(Anonymouos Class)
익명 클래스(Anonymous Class) 는 말 그대로 이름이 없는 클래스입니다. 주로 일회성으로 인터페이스나 추상 클래스를 구현해야 할 때 사용합니다.
- 클래스 정의 + 객체 생성을 동시에 함
- 이름이 없으므로 재사용은 불가능
- 추상 클래스나 인터페이스를 빠르게 구현할 때 유용
- 보통 콜백(callback), 이벤트 핸들러, 쓰레드 실행 등에 자주 쓰임
- 일반적은 클래스 정의 및 사용
```java
public class DripCoffeeMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing with DripCoffeeMachine";
    }
}

CoffeeMachine coffeeMachine = new DripCoffeeMachine();
```
- 람다 기본 문법
```java
new 부모클래스명() {
    // 메서드 재정의
};

new 인터페이스명() {
    // 인터페이스 메서드 구현
};
```
예시, Runnable 인터페이스를 구현한 클래스를 작성하고 new 새로작성한클래스()와 같이 사용하지 않고 다음과 같이 바로 즉석에서 객체를 생성함. 
```java
// 익명 클래스 사용
CoffeeMachine dripCoffeeMachine = new CoffeeMachine() {
    @Override
    public void brew() {
        System.out.println("Brewing with DripCoffeeMachine");
    }
};
```
# 익명 클래스 특징
## 장점
- 빠르게 구현 가능 → 별도의 클래스 파일 불필요.
- 콜백 로직 같은 일회성 코드 작성에 적합.
## 단점
- 코드가 길어지면 가독성 떨어짐.
- 재사용 불가능.
## Java 8 이후엔 람다(Lambda) 로 대체되는 경우가 많음
- 익명 클래스는 범용적이지만 코드가 장황함.
- 람다는 함수형 인터페이스(추상 메서드를 딱 하나만 가진 인터페이스)일 때만 사용 가능하지만 훨씬 간결함.
- 실무에서는 가급적 람다를 쓰고, 불가피할 때만 익명 클래스를 씀.
```java
// 전통적인 방식: 익명 클래스
CoffeeMachine dripCoffeeMachine = new CoffeeMachine() {
    @Override
    public void brew() {
        System.out.println("Brewing with DripCoffeeMachine");
    }
};

// 람다식 사용
CoffeeMachine dripCoffeeMachine =  () -> System.out.println("Brewing with DripCoffeeMachine");
// (매개변수) -> { 실행문 }
// 매개변수 타입 생략 (대입문 앞에서 받는 인터페이스를 보면 알 수 있기 때문)
// 매개변수가 1개면 괄호 () 생략 가능
// 실행문이 1개면 중괄호 {}와 return 생략 가능
```
## 스프링 시큐리티의 필터체인
각 필터들의 설정은 대부분 매개변수 하나를 갖는 메서드 하나만 존재하는 인터페이스를 구현하는 것이므로 람다식을 사용하는 것이 일반적이다.

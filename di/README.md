
## 일반적으로 클래스를 생성하고 사용하는 패턴
```java
public class DripCoffeeMachine {
    public String brew() {
        return "Brewing coffee with Drip Coffee Machine";
    }
}

public class Barista {
    private DripCoffeeMachine coffeeMachine;

    public LegacyBarista() {
        coffeeMachine = new DripCoffeeMachine();
    }

    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }
}

public class Main {
    public static void main(String[] args) {
        LegacyBarista barista = new LegacyBarista();
        barista.makeCoffee();
    }
}
```

## Dependency Injection (의존성 주입)
- 공통의 규격을 인터페이스를 사용하여 정의 
```java
public interface CoffeeMachine {
    String brew();
}

public class DripCoffeeMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Drip Coffee Machine";
    }
}
```
- 내가 사용할 클래스를 직접 생성하지 않고 외부에서 주입(DI)하도록 변경, 이때 특정 클래스를 직접 사용하는대신 공통의 규격인 인터페이스를 사용하여 느슨한 결합으로 바꿈
```java
public class Barista {
    private CoffeeMachine coffeeMachine;

    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }
}
```
- Main에서 필요한 의존성을 주입하고 실행
```java
public class Main {
    public static void main(String[] args) {
        Barista barista = new Barista();
        barista.setCoffeeMachine(new DripCoffeeMachine());
        barista.makeCoffee();
    }
}
```
- 이후에는 어떠한 커피머신도 공통 규격만 구현한다면 Main에서 주입할 수 있다.
```java
public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Espresso Machine";
    }
}

public class Main {
    public static void main(String[] args) {
        Barista barista = new Barista();
        barista.setCoffeeMachine(new EspressoMachine());
        barista.makeCoffee();
    }
}
```
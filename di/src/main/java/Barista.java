public class Barista {
    private CoffeeMachine coffeeMachine;

    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }
}

public class Main {
    public static void main(String[] args) {
        Barista barista = new Barista();
        barista.setCoffeeMachine(new DripCoffeeMachine());
        barista.makeCoffee();
    }
}


interface Burger {
    void prepare();
}

class BasicBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Basic Burger with bun, patty, and ketchup!");
    }
}

class StandardBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Standard Burger with bun, patty, cheese and lettuce!");
    }
}

class PremiumBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Premium Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}

class BurgerFactory {

    public Burger createBurger(String type) {

        switch (type.toLowerCase()) {

            case "basic":
                return new BasicBurger();

            case "standard":
                return new StandardBurger();

            case "premium":
                return new PremiumBurger();

            default:
                System.out.println("Invalid burger type!");
                return null;
        }
    }
}

public class SimpleFactory {

    public static void main(String[] args) {

        String type = "standard";

        BurgerFactory burgerFactory =
                new BurgerFactory();

        Burger burger =
                burgerFactory.createBurger(type);

        if (burger != null) {
            burger.prepare();
        }
    }
}


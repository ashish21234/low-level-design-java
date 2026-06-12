
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
class BasicWheatBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Basic wheat Burger with bun, patty, and ketchup!");
    }
}

class StandardWheatBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Standard wheat Burger with bun, patty, cheese and lettuce!");
    }
}

class PremiumWheatBurger implements Burger {

    @Override
    public void prepare() {
        System.out.println(
                "Preparing Premium wheat Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}
interface BurgerFactory{
    Burger createBurger(String type);
}

class SinghBurger implements BurgerFactory {

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

class KingBurger implements BurgerFactory {

    public Burger createBurger(String type) {

        switch (type.toLowerCase()) {

            case "basic":
                return new BasicWheatBurger();

            case "standard":
                return new StandardWheatBurger();

            case "premium":
                return new PremiumWheatBurger();

            default:
                System.out.println("Invalid burger type!");
                return null;
        }
    }
}

public class FactoryMethod {
    public static void main(String[] args) {
        String type = "basic";

        BurgerFactory myFactory = new SinghBurger();
        Burger burger = myFactory.createBurger(type);

        if (burger != null) {
            burger.prepare();
        }
    }
}


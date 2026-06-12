
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

interface GarlicBread {
    void prepare();
}

class BasicGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Basic Garlic Bread with butter and garlic!");
    }
}

class CheeseGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Cheese Garlic Bread with extra cheese and butter!");
    }
}

class BasicWheatGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Basic Wheat Garlic Bread with butter and garlic!");
    }
}

class CheeseWheatGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Cheese Wheat Garlic Bread with extra cheese and butter!");
    }
}

interface MealFactory{
    Burger createBurger(String type);
    GarlicBread createGarlicBread(String type);
}

class SinghBurger implements MealFactory {

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

     public GarlicBread createGarlicBread(String type) {
        if (type.equalsIgnoreCase("basic")) {
            return new BasicGarlicBread();
        } else if (type.equalsIgnoreCase("cheese")) {
            return new CheeseGarlicBread();
        } else {
            System.out.println("Invalid Garlic bread type!");
            return null;
        }
    }
}

class KingBurger implements MealFactory {

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
     public GarlicBread createGarlicBread(String type) {
        if (type.equalsIgnoreCase("basic")) {
            return new BasicWheatGarlicBread();
        } else if (type.equalsIgnoreCase("cheese")) {
            return new CheeseWheatGarlicBread();
        } else {
            System.out.println("Invalid Garlic bread type!");
            return null;
        }
    }
}

public class AbstractFactory {
    public static void main(String[] args) {
        String burgerType = "basic";
        String garlicBreadType = "cheese";

        MealFactory mealFactory = new SinghBurger();

        Burger burger = mealFactory.createBurger(burgerType);
        GarlicBread garlicBread = mealFactory.createGarlicBread(garlicBreadType);

        if (burger != null) burger.prepare();
        if (garlicBread != null) garlicBread.prepare();
    }
}


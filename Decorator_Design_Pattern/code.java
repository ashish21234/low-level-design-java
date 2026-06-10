
interface GameCharacter {
    String getAbilities();
}

class Mario implements GameCharacter {

    @Override
    public String getAbilities() {
        return "Mario";
    }
}

abstract class Decorator implements GameCharacter {

    protected GameCharacter character;

    public Decorator(GameCharacter character) {
        this.character = character;
    }
}

class HeightUp extends Decorator {

    public HeightUp(GameCharacter character) {
        super(character);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities() + " + Height Up";
    }
}

class GunPowerUp extends Decorator {

    public GunPowerUp(GameCharacter character) {
        super(character);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities() + " + Gun";
    }
}

class StarPowerUp extends Decorator {

    public StarPowerUp(GameCharacter character) {
        super(character);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities()
                + " + Star Power (Limited Time)";
    }
}

public class DecoratorPattern {

    public static void main(String[] args) {

        GameCharacter mario = new Mario();
        System.out.println(
                "Basic Character: "
                        + mario.getAbilities());

        mario = new HeightUp(mario);
        System.out.println(
                "After HeightUp: "
                        + mario.getAbilities());

        mario = new GunPowerUp(mario);
        System.out.println(
                "After GunPowerUp: "
                        + mario.getAbilities());

        mario = new StarPowerUp(mario);
        System.out.println(
                "After StarPowerUp: "
                        + mario.getAbilities());
    }
}


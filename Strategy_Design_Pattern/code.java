```java
interface Talkable {
    void talk();
}

class NormalTalk implements Talkable {
    public void talk() {
        System.out.println("Talking normally...");
    }
}

class NoTalk implements Talkable {
    public void talk() {
        System.out.println("No talking");
    }
}

interface Walkable {
    void walk();
}

class NormalWalk implements Walkable {
    public void walk() {
        System.out.println("Walking normally...");
    }
}

class NoWalk implements Walkable {
    public void walk() {
        System.out.println("No walking");
    }
}

interface Flyable {
    void fly();
}

class NormalFly implements Flyable {
    public void fly() {
        System.out.println("Flying normally...");
    }
}

class NoFly implements Flyable {
    public void fly() {
        System.out.println("No flying");
    }
}

abstract class Robot {
    protected Talkable talkBehavior;
    protected Walkable walkBehavior;
    protected Flyable flyBehavior;

    public Robot(Walkable w, Talkable t, Flyable f) {
        this.walkBehavior = w;
        this.talkBehavior = t;
        this.flyBehavior = f;
    }

    public void walk() {
        walkBehavior.walk();
    }

    public void talk() {
        talkBehavior.talk();
    }

    public void fly() {
        flyBehavior.fly();
    }

    public abstract void projection();
}

class CompanionRobot extends Robot {

    public CompanionRobot(Walkable w, Talkable t, Flyable f) {
        super(w, t, f);
    }

    @Override
    public void projection() {
        System.out.println("Displaying friendly companion features...");
    }
}

class WorkerRobot extends Robot {

    public WorkerRobot(Walkable w, Talkable t, Flyable f) {
        super(w, t, f);
    }

    @Override
    public void projection() {
        System.out.println("Displaying worker efficiency stats...");
    }
}

public class StrategyDesignPattern {

    public static void main(String[] args) {

        Robot robot1 = new CompanionRobot(
                new NormalWalk(),
                new NormalTalk(),
                new NoFly());

        robot1.walk();
        robot1.talk();
        robot1.fly();
        robot1.projection();

        System.out.println("--------------------");

        Robot robot2 = new WorkerRobot(
                new NoWalk(),
                new NoTalk(),
                new NormalFly());

        robot2.walk();
        robot2.talk();
        robot2.fly();
        robot2.projection();
    }
}
```

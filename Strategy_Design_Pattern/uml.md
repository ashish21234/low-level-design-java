# Strategy Design Pattern - Robot Example

```mermaid
classDiagram

class Talkable {
    <<interface>>
    +talk()
}

class Walkable {
    <<interface>>
    +walk()
}

class Flyable {
    <<interface>>
    +fly()
}

class NormalTalk
class NoTalk
class NormalWalk
class NoWalk
class NormalFly
class NoFly

Talkable <|.. NormalTalk
Talkable <|.. NoTalk

Walkable <|.. NormalWalk
Walkable <|.. NoWalk

Flyable <|.. NormalFly
Flyable <|.. NoFly

class Robot {
    #Talkable talkBehavior
    #Walkable walkBehavior
    #Flyable flyBehavior
    +walk()
    +talk()
    +fly()
    +projection()
}

class CompanionRobot {
    +projection()
}

class WorkerRobot {
    +projection()
}

Robot <|-- CompanionRobot
Robot <|-- WorkerRobot

Robot --> Talkable
Robot --> Walkable
Robot --> Flyable
```

## Strategy Pattern

* `Talkable`, `Walkable`, and `Flyable` are strategy interfaces.
* `NormalTalk`, `NoTalk`, `NormalWalk`, `NoWalk`, `NormalFly`, and `NoFly` are concrete strategies.
* `Robot` is the context class.
* `CompanionRobot` and `WorkerRobot` are specialized robot types.
* Behaviors can be changed by passing different strategy implementations to the constructor.

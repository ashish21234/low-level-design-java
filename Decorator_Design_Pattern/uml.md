# Decorator Design Pattern - Mario Power Ups

```mermaid
classDiagram

class GameCharacter {
    <<interface>>
    +getAbilities() String
}

class Mario {
    +getAbilities() String
}

class Decorator {
    <<abstract>>
    #GameCharacter character
    +Decorator(GameCharacter)
}

class HeightUp {
    +getAbilities() String
}

class GunPowerUp {
    +getAbilities() String
}

class StarPowerUp {
    +getAbilities() String
}

GameCharacter <|.. Mario
GameCharacter <|.. Decorator

Decorator <|-- HeightUp
Decorator <|-- GunPowerUp
Decorator <|-- StarPowerUp

Decorator --> GameCharacter
```

## Pattern Components

### Component

* `GameCharacter`
* Defines the common interface.

### Concrete Component

* `Mario`
* Base object that can be decorated.

### Decorator

* `Decorator`
* Holds a reference to a `GameCharacter`.

### Concrete Decorators

* `HeightUp`
* `GunPowerUp`
* `StarPowerUp`

Each decorator adds new abilities while preserving existing behavior.

## Example Flow

```text
Mario
   ↓
HeightUp
   ↓
GunPowerUp
   ↓
StarPowerUp
```

Final Output:

```text
Mario + Height Up + Gun + Star Power (Limited Time)
```

This demonstrates how functionality can be added dynamically without modifying the original `Mario` class.

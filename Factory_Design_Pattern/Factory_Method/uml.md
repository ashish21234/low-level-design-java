# Factory Method Pattern - Burger Store Example

```mermaid
classDiagram

class Burger {
    <<interface>>
    +prepare()
}

class BasicBurger {
    +prepare()
}

class StandardBurger {
    +prepare()
}

class PremiumBurger {
    +prepare()
}

class BasicWheatBurger {
    +prepare()
}

class StandardWheatBurger {
    +prepare()
}

class PremiumWheatBurger {
    +prepare()
}

class BurgerFactory {
    <<interface>>
    +createBurger(String) Burger
}

class SinghBurger {
    +createBurger(String) Burger
}

class KingBurger {
    +createBurger(String) Burger
}

Burger <|.. BasicBurger
Burger <|.. StandardBurger
Burger <|.. PremiumBurger

Burger <|.. BasicWheatBurger
Burger <|.. StandardWheatBurger
Burger <|.. PremiumWheatBurger

BurgerFactory <|.. SinghBurger
BurgerFactory <|.. KingBurger

SinghBurger ..> BasicBurger : creates
SinghBurger ..> StandardBurger : creates
SinghBurger ..> PremiumBurger : creates

KingBurger ..> BasicWheatBurger : creates
KingBurger ..> StandardWheatBurger : creates
KingBurger ..> PremiumWheatBurger : creates
```

## Pattern Components

### Product Interface

* `Burger`

### Concrete Products

#### Regular Burger Family

* `BasicBurger`
* `StandardBurger`
* `PremiumBurger`

#### Wheat Burger Family

* `BasicWheatBurger`
* `StandardWheatBurger`
* `PremiumWheatBurger`

### Creator Interface

* `BurgerFactory`

Defines:

```java
Burger createBurger(String type);
```

### Concrete Factories

#### SinghBurger Factory

Creates:

* BasicBurger
* StandardBurger
* PremiumBurger

#### KingBurger Factory

Creates:

* BasicWheatBurger
* StandardWheatBurger
* PremiumWheatBurger

## Flow

```text
Client
   |
   v
BurgerFactory
   |
   +---- SinghBurger
   |          |
   |          +---- BasicBurger
   |          +---- StandardBurger
   |          +---- PremiumBurger
   |
   +---- KingBurger
              |
              +---- BasicWheatBurger
              +---- StandardWheatBurger
              +---- PremiumWheatBurger
```

## Why Factory Method?

Instead of:

```java
Burger burger = new BasicBurger();
```

Client writes:

```java
BurgerFactory factory = new SinghBurger();
Burger burger = factory.createBurger("basic");
```

The client depends only on:

* `Burger`
* `BurgerFactory`

and remains independent of concrete burger implementations.

## Output

```text
Preparing Basic Burger with bun, patty, and ketchup!
```

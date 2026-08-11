# Parking Lot System — UML Diagram

## Design Patterns Used

* **Strategy Pattern**

  * `ParkingFeeStrategy`
  * `BasicHourlyRateStrategy`
  * `PremiumRateStrategy`
  * `PaymentStrategy`
  * `CashPayment`
  * `CreditCardPayment`

* **Factory Pattern**

  * `VehicleFactory`

* **Inheritance**

  * `Vehicle` → `CarVehicle`, `BikeVehicle`, `OtherVehicle`
  * `ParkingSpot` → `CarParkingSpot`, `BikeParkingSpot`

* **Composition**

  * `ParkingLot` contains multiple `ParkingSpot` objects.

---

## Class Diagram

```plantuml
@startuml

skinparam classAttributeIconSize 0

title Parking Lot System - Low Level Design

' =====================================================
' ENUM
' =====================================================

enum DurationType {
    HOURS
    DAYS
}


' =====================================================
' PARKING FEE STRATEGY
' =====================================================

interface ParkingFeeStrategy {
    +calculateFee(
        vehicleType : String,
        duration : int,
        durationType : DurationType
    ) : double
}

class BasicHourlyRateStrategy {
    +calculateFee(
        vehicleType : String,
        duration : int,
        durationType : DurationType
    ) : double
}

class PremiumRateStrategy {
    +calculateFee(
        vehicleType : String,
        duration : int,
        durationType : DurationType
    ) : double
}

ParkingFeeStrategy <|.. BasicHourlyRateStrategy
ParkingFeeStrategy <|.. PremiumRateStrategy


' =====================================================
' VEHICLE HIERARCHY
' =====================================================

abstract class Vehicle {
    -licensePlate : String
    -vehicleType : String
    -feeStrategy : ParkingFeeStrategy

    +Vehicle(
        licensePlate : String,
        vehicleType : String,
        feeStrategy : ParkingFeeStrategy
    )

    +getVehicleType() : String
    +getLicensePlate() : String
    +calculateFee(
        duration : int,
        durationType : DurationType
    ) : double
}

class CarVehicle {
    +CarVehicle(
        lp : String,
        vt : String,
        fs : ParkingFeeStrategy
    )
}

class BikeVehicle {
    +BikeVehicle(
        lp : String,
        vt : String,
        fs : ParkingFeeStrategy
    )
}

class OtherVehicle {
    +OtherVehicle(
        lp : String,
        vt : String,
        fs : ParkingFeeStrategy
    )
}

Vehicle <|-- CarVehicle
Vehicle <|-- BikeVehicle
Vehicle <|-- OtherVehicle

Vehicle --> ParkingFeeStrategy : uses
Vehicle --> DurationType : uses


' =====================================================
' VEHICLE FACTORY
' =====================================================

class VehicleFactory {
    +createVehicle(
        vehicleType : String,
        licensePlate : String,
        feeStrategy : ParkingFeeStrategy
    ) : Vehicle
}

VehicleFactory ..> Vehicle : creates
VehicleFactory ..> CarVehicle : creates
VehicleFactory ..> BikeVehicle : creates
VehicleFactory ..> OtherVehicle : creates


' =====================================================
' PAYMENT STRATEGY
' =====================================================

interface PaymentStrategy {
    +processPayment(amount : double)
}

class CashPayment {
    +processPayment(amount : double)
}

class CreditCardPayment {
    +processPayment(amount : double)
}

PaymentStrategy <|.. CashPayment
PaymentStrategy <|.. CreditCardPayment


' =====================================================
' PAYMENT
' =====================================================

class Payment {
    -amount : double
    -strategy : PaymentStrategy

    +Payment(
        amount : double,
        strategy : PaymentStrategy
    )

    +processPayment()
}

Payment --> PaymentStrategy : delegates to


' =====================================================
' PARKING SPOT HIERARCHY
' =====================================================

abstract class ParkingSpot {
    -spotNumber : int
    -occupied : boolean
    -vehicle : Vehicle
    -spotType : String

    +ParkingSpot(
        spotNumber : int,
        spotType : String
    )

    +isOccupied() : boolean
    +getSpotNumber() : int
    +getVehicle() : Vehicle
    +getSpotType() : String

    +canParkVehicle(
        vehicle : Vehicle
    ) : boolean

    +parkVehicle(
        vehicle : Vehicle
    )

    +vacate()
}

class CarParkingSpot {
    +CarParkingSpot(
        no : int,
        type : String
    )

    +canParkVehicle(
        vehicle : Vehicle
    ) : boolean
}

class BikeParkingSpot {
    +BikeParkingSpot(
        no : int,
        type : String
    )

    +canParkVehicle(
        vehicle : Vehicle
    ) : boolean
}

ParkingSpot <|-- CarParkingSpot
ParkingSpot <|-- BikeParkingSpot

ParkingSpot --> Vehicle : parks


' =====================================================
' PARKING LOT
' =====================================================

class ParkingLot {
    -spots : List<ParkingSpot>

    +ParkingLot(
        spots : List<ParkingSpot>
    )

    +findAvailableSpot(
        vehicleType : String
    ) : ParkingSpot

    +parkVehicle(
        vehicle : Vehicle
    ) : ParkingSpot

    +vacateSpot(
        spot : ParkingSpot,
        vehicle : Vehicle
    )

}

ParkingLot *-- "1..*" ParkingSpot : contains
ParkingLot --> Vehicle : parks


' =====================================================
' MAIN
' =====================================================

class ParkingLotSystem {
    +main(args : String[])
    -getPaymentStrategy(
        choice : int
    ) : PaymentStrategy
}

ParkingLotSystem ..> ParkingLot
ParkingLotSystem ..> VehicleFactory
ParkingLotSystem ..> ParkingFeeStrategy
ParkingLotSystem ..> Vehicle
ParkingLotSystem ..> Payment
ParkingLotSystem ..> PaymentStrategy
ParkingLotSystem ..> DurationType


@enduml
```

---

## Main Relationships

### 1. Fee Strategy

```text
ParkingFeeStrategy
        ▲
        │ implements
        │
 ┌──────┴─────────────────┐
 │                        │
BasicHourlyRateStrategy  PremiumRateStrategy
```

`Vehicle` contains a `ParkingFeeStrategy`, so the fee calculation can be changed without modifying the `Vehicle` class.

---

### 2. Vehicle Factory

```text
             VehicleFactory
                   │
                creates
                   │
          ┌────────┼────────┐
          ▼        ▼        ▼
        Car      Bike     Other
      Vehicle   Vehicle   Vehicle
```

The client does not directly need to instantiate the concrete vehicle classes.

---

### 3. Payment Strategy

```text
              PaymentStrategy
                    ▲
                    │
          implements│
            ┌───────┴────────┐
            │                │
      CashPayment      CreditCardPayment
```

`Payment` acts as the **Context**:

```text
Payment
   │
   │ has-a
   ▼
PaymentStrategy
```

The actual payment operation is delegated to the selected strategy.

---

### 4. Parking Spot Hierarchy

```text
             ParkingSpot
                  ▲
                  │
          ┌───────┴────────┐
          │                │
   CarParkingSpot   BikeParkingSpot
```

Each parking spot determines whether a particular vehicle can park there.

---

### 5. Parking Lot Composition

```text
ParkingLot
    │
    │ contains
    ▼
ParkingSpot
```

The `ParkingLot` maintains a collection:

```java
private List<ParkingSpot> spots;
```

Therefore, the UML uses **composition**:

```text
ParkingLot *-- ParkingSpot
```

---

## Overall Architecture

```text
                         ┌───────────────────────┐
                         │   ParkingLotSystem    │
                         └───────────┬───────────┘
                                     │
                  ┌──────────────────┼──────────────────┐
                  │                  │                  │
                  ▼                  ▼                  ▼
          VehicleFactory        ParkingLot          Payment
                  │                  │                  │
                  ▼                  ▼                  ▼
              Vehicle          ParkingSpot      PaymentStrategy
                  │                  │                  │
                  ▼                  ▼                  ▼
        ParkingFeeStrategy    Car/Bike Spot     Cash/CreditCard
```

---

## Patterns Summary

| Pattern     | Classes                      | Purpose                               |
| ----------- | ---------------------------- | ------------------------------------- |
| Strategy    | `ParkingFeeStrategy`         | Different fee calculation algorithms  |
| Strategy    | `PaymentStrategy`            | Different payment methods             |
| Factory     | `VehicleFactory`             | Creates appropriate vehicle objects   |
| Inheritance | `Vehicle` hierarchy          | Represents different vehicle types    |
| Inheritance | `ParkingSpot` hierarchy      | Represents different parking spots    |
| Composition | `ParkingLot` → `ParkingSpot` | Parking lot manages its parking spots |

---

## Important Note

The current implementation uses:

```java
private static PaymentStrategy getPaymentStrategy(int choice)
```

inside `ParkingLotSystem` to select the payment strategy.

If you later introduce a separate `PaymentFactory`, the UML should be updated to:

```text
ParkingLotSystem
       │
       ▼
PaymentFactory
       │
       ▼
PaymentStrategy
```

That would make the payment-strategy creation responsibility cleaner and remove the strategy-selection logic from `ParkingLotSystem`.

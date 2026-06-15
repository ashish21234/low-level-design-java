# Parking Lot Management System - UML Class Diagram

## Overview

This system demonstrates the use of:

* **Factory Pattern** (`VehicleFactory`)
* **Strategy Pattern** (`ParkingFeeStrategy`, `PaymentStrategy`)
* **Inheritance** (`Vehicle`, `ParkingSpot`)
* **Composition/Aggregation** (`ParkingLot`, `Payment`)

---

## Class Diagram (PlantUML)

```plantuml
@startuml

enum DurationType {
    HOURS
    DAYS
}

interface ParkingFeeStrategy {
    +calculateFee(vehicleType : String,
                  duration : int,
                  durationType : DurationType) : double
}

class BasicHourlyRateStrategy
class PremiumRateStrategy

ParkingFeeStrategy <|.. BasicHourlyRateStrategy
ParkingFeeStrategy <|.. PremiumRateStrategy

abstract class Vehicle {
    -licensePlate : String
    -vehicleType : String
    -feeStrategy : ParkingFeeStrategy

    +getVehicleType() : String
    +getLicensePlate() : String
    +calculateFee(duration : int,
                  durationType : DurationType) : double
}

class CarVehicle
class BikeVehicle
class OtherVehicle

Vehicle <|-- CarVehicle
Vehicle <|-- BikeVehicle
Vehicle <|-- OtherVehicle

Vehicle --> ParkingFeeStrategy

class VehicleFactory {
    +createVehicle(vehicleType,
                   licensePlate,
                   feeStrategy) : Vehicle
}

VehicleFactory ..> Vehicle

interface PaymentStrategy {
    +processPayment(amount : double)
}

class CashPayment
class CreditCardPayment

PaymentStrategy <|.. CashPayment
PaymentStrategy <|.. CreditCardPayment

class Payment {
    -amount : double
    -paymentStrategy : PaymentStrategy

    +processPayment()
}

Payment --> PaymentStrategy

abstract class ParkingSpot {
    -spotNumber : int
    -isOccupied : boolean
    -vehicle : Vehicle
    -spotType : String

    +isOccupied() : boolean
    +parkVehicle(vehicle)
    +vacate()
    +canParkVehicle(vehicle)
}

class CarParkingSpot
class BikeParkingSpot

ParkingSpot <|-- CarParkingSpot
ParkingSpot <|-- BikeParkingSpot

ParkingSpot --> Vehicle

class ParkingLot {
    -parkingSpots : List<ParkingSpot>

    +findAvailableSpot(vehicleType)
    +parkVehicle(vehicle)
    +vacateSpot(spot, vehicle)
    +getSpotByNumber(spotNumber)
}

ParkingLot *-- ParkingSpot
ParkingLot --> Vehicle

class ParkingLotMain

ParkingLotMain ..> ParkingLot
ParkingLotMain ..> VehicleFactory
ParkingLotMain ..> Payment
ParkingLotMain ..> PaymentStrategy

@enduml
```

---

## Design Patterns Used

### Factory Pattern

**VehicleFactory**

Responsible for creating different types of vehicles:

* CarVehicle
* BikeVehicle
* OtherVehicle

This hides object creation logic from the client.

---

### Strategy Pattern

#### Parking Fee Strategy

Interface:

```java
ParkingFeeStrategy
```

Implementations:

* BasicHourlyRateStrategy
* PremiumRateStrategy

Allows parking fee calculation logic to vary independently from vehicle classes.

---

#### Payment Strategy

Interface:

```java
PaymentStrategy
```

Implementations:

* CashPayment
* CreditCardPayment

Allows different payment methods without modifying existing code.

---

## Relationships

### Inheritance

* Vehicle ← CarVehicle

* Vehicle ← BikeVehicle

* Vehicle ← OtherVehicle

* ParkingSpot ← CarParkingSpot

* ParkingSpot ← BikeParkingSpot

### Realization

* ParkingFeeStrategy ← BasicHourlyRateStrategy

* ParkingFeeStrategy ← PremiumRateStrategy

* PaymentStrategy ← CashPayment

* PaymentStrategy ← CreditCardPayment

### Composition

* ParkingLot contains ParkingSpot objects

### Association

* ParkingSpot ↔ Vehicle
* Payment ↔ PaymentStrategy
* Vehicle ↔ ParkingFeeStrategy

---

## Suggested Improvements

1. Replace String vehicle types with an enum:

```java
enum VehicleType {
    CAR,
    BIKE,
    AUTO
}
```

2. Introduce a PaymentStrategyFactory.

3. Add a ParkingTicket class containing:

* ticketId
* vehicle
* spot
* entryTime
* exitTime

4. Calculate parking fees automatically using entry and exit timestamps instead of hardcoded durations.

```
```

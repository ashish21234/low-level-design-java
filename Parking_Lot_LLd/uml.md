# Parking Lot System UML

```mermaid
classDiagram

%% ====================
%% ENUM
%% ====================

class DurationType {
    <<enumeration>>
    HOURS
    DAYS
}

%% ====================
%% FEE STRATEGY
%% ====================

class ParkingFeeStrategy {
    <<interface>>
    +calculateFee(vehicleType, duration, durationType)
}

class BasicHourlyRateStrategy
class PremiumRateStrategy

ParkingFeeStrategy <|.. BasicHourlyRateStrategy
ParkingFeeStrategy <|.. PremiumRateStrategy

%% ====================
%% VEHICLES
%% ====================

class Vehicle {
    <<abstract>>
    -licensePlate : String
    -vehicleType : String
    -feeStrategy : ParkingFeeStrategy

    +getVehicleType()
    +getLicensePlate()
    +calculateFee(duration, durationType)
}

class CarVehicle
class BikeVehicle
class OtherVehicle

Vehicle <|-- CarVehicle
Vehicle <|-- BikeVehicle
Vehicle <|-- OtherVehicle

Vehicle --> ParkingFeeStrategy

class VehicleFactory {
    +createVehicle(vehicleType, licensePlate, feeStrategy)
}

VehicleFactory ..> Vehicle

%% ====================
%% PAYMENT STRATEGY
%% ====================

class PaymentStrategy {
    <<interface>>
    +processPayment(amount)
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

%% ====================
%% PARKING SPOTS
%% ====================

class ParkingSpot {
    <<abstract>>
    -spotNumber : int
    -isOccupied : boolean
    -vehicle : Vehicle
    -spotType : String

    +isOccupied()
    +parkVehicle(vehicle)
    +vacate()
    +canParkVehicle(vehicle)
}

class CarParkingSpot
class BikeParkingSpot

ParkingSpot <|-- CarParkingSpot
ParkingSpot <|-- BikeParkingSpot

ParkingSpot --> Vehicle

%% ====================
%% PARKING LOT
%% ====================

class ParkingLot {
    -parkingSpots : List~ParkingSpot~

    +findAvailableSpot(vehicleType)
    +parkVehicle(vehicle)
    +vacateSpot(spot, vehicle)
    +getSpotByNumber(spotNumber)
}

ParkingLot *-- ParkingSpot
ParkingLot --> Vehicle

%% ====================
%% MAIN
%% ====================

class ParkingLotMain

ParkingLotMain ..> ParkingLot
ParkingLotMain ..> VehicleFactory
ParkingLotMain ..> Payment
ParkingLotMain ..> PaymentStrategy
```

## Design Patterns Used

### Strategy Pattern

#### Parking Fee Calculation

* ParkingFeeStrategy
* BasicHourlyRateStrategy
* PremiumRateStrategy

Allows different fee calculation algorithms.

#### Payment Processing

* PaymentStrategy
* CashPayment
* CreditCardPayment

Allows switching payment methods at runtime.

---

### Factory Pattern

* VehicleFactory

Responsible for creating different vehicle types:

* CarVehicle
* BikeVehicle
* OtherVehicle

without exposing creation logic to the client.

---

### Composition

* ParkingLot contains multiple ParkingSpots.

```text
ParkingLot
    |
    +---- ParkingSpot
                |
                +---- CarParkingSpot
                +---- BikeParkingSpot
```

---

### Relationships

#### Vehicle → ParkingFeeStrategy

Each vehicle uses a fee calculation strategy.

#### Payment → PaymentStrategy

Each payment delegates processing to a payment strategy.

#### ParkingSpot → Vehicle

A parking spot can contain a parked vehicle.

#### ParkingLot → ParkingSpot

A parking lot manages multiple parking spots.

---

## Flow

```text
VehicleFactory
      |
      v
    Vehicle
      |
      v
ParkingLot ----> ParkingSpot

Vehicle
   |
   +---- ParkingFeeStrategy

Payment
   |
   +---- PaymentStrategy
```

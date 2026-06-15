# Parking Lot System - UML Diagram

This UML diagram represents a Parking Lot System designed using Factory and Strategy Design Patterns.

## UML Diagram (PlantUML)

```plantuml
@startuml

'====================
' ENUMS
'====================

enum DurationType {
    HOURS
    DAYS
}

'====================
' FEE STRATEGY
'====================

interface ParkingFeeStrategy {
    +calculateFee(vehicleType : String,
                  duration : int,
                  durationType : DurationType) : double
}

class BasicHourlyRateStrategy
class PremiumRateStrategy

ParkingFeeStrategy <|.. BasicHourlyRateStrategy
ParkingFeeStrategy <|.. PremiumRateStrategy

'====================
' VEHICLES
'====================

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

Vehicle --> ParkingFeeStrategy : uses

class VehicleFactory {
    +createVehicle(vehicleType,
                   licensePlate,
                   feeStrategy) : Vehicle
}

VehicleFactory ..> Vehicle

'====================
' PAYMENT STRATEGY
'====================

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

'====================
' PARKING SPOTS
'====================

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

'====================
' PARKING LOT
'====================

class ParkingLot {
    -parkingSpots : List<ParkingSpot>

    +findAvailableSpot(vehicleType)
    +parkVehicle(vehicle)
    +vacateSpot(spot, vehicle)
    +getSpotByNumber(spotNumber)
}

ParkingLot *-- ParkingSpot
ParkingLot --> Vehicle

'====================
' MAIN
'====================

class ParkingLotMain

ParkingLotMain ..> ParkingLot
ParkingLotMain ..> VehicleFactory
ParkingLotMain ..> Payment
ParkingLotMain ..> PaymentStrategy

@enduml
```

## Design Patterns Used

### Factory Pattern
- `VehicleFactory`
- Responsible for creating different vehicle objects (`CarVehicle`, `BikeVehicle`, `OtherVehicle`).
- Encapsulates object creation logic.

### Strategy Pattern
#### Parking Fee Strategy
- `ParkingFeeStrategy`
- `BasicHourlyRateStrategy`
- `PremiumRateStrategy`

Allows fee calculation logic to be changed without modifying vehicle classes.

#### Payment Strategy
- `PaymentStrategy`
- `CashPayment`
- `CreditCardPayment`

Allows different payment methods to be plugged in dynamically.

## Key Components

| Component | Responsibility |
|------------|---------------|
| Vehicle | Represents a parked vehicle |
| VehicleFactory | Creates vehicle objects |
| ParkingSpot | Represents a parking slot |
| ParkingLot | Manages parking spots and parking operations |
| ParkingFeeStrategy | Calculates parking fees |
| PaymentStrategy | Handles payment processing |
| Payment | Executes payment using a chosen strategy |

## Relationships

- Vehicle uses ParkingFeeStrategy.
- Payment uses PaymentStrategy.
- ParkingLot contains multiple ParkingSpots.
- ParkingSpot can hold one Vehicle.
- VehicleFactory creates Vehicle objects.

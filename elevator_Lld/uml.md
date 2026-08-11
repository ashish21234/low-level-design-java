# Elevator System — Low Level Design

## Design Patterns Used

This Elevator System uses the following design patterns:

1. **Observer Pattern**
   - `ElevatorObserver`
   - `ElevatorDisplay`
   - `Elevator`
   
   Used to notify displays/monitoring components whenever an elevator changes its state or floor.

2. **Command Pattern**
   - `ElevatorCommand`
   - `ElevatorRequest`
   
   Used to encapsulate elevator requests as command objects.

3. **Strategy Pattern**
   - `SchedulingStrategy`
   - `FCFSSchedulingStrategy`
   - `ScanSchedulingStrategy`
   - `LookSchedulingStrategy`
   
   Used to allow different elevator scheduling algorithms to be selected dynamically.

4. **Factory Pattern**
   - `ElevatorFactory`
   - `Elevator`
   - `ExpressElevator`
   
   Used to create different types of elevators without directly coupling the client to their concrete classes.

---

# UML Class Diagram

```plantuml
@startuml

title Elevator System - Low Level Design

skinparam classAttributeIconSize 0

' =====================================================
' ENUMS
' =====================================================

enum Direction {
    UP
    DOWN
    IDLE
}

enum ElevatorState {
    IDLE
    MOVING
    STOPPED
    MAINTENANCE
}


' =====================================================
' OBSERVER PATTERN
' =====================================================

interface ElevatorObserver {

    +onElevatorStateChange(
        elevator : Elevator,
        state : ElevatorState
    )

    +onElevatorFloorChange(
        elevator : Elevator,
        floor : int
    )
}

class ElevatorDisplay {

    +onElevatorStateChange(
        elevator : Elevator,
        state : ElevatorState
    )

    +onElevatorFloorChange(
        elevator : Elevator,
        floor : int
    )
}

ElevatorObserver <|.. ElevatorDisplay


' =====================================================
' COMMAND PATTERN
' =====================================================

interface ElevatorCommand {

    +execute()
}

class ElevatorRequest {

    -elevatorId : int
    -floor : int
    -requestDirection : Direction
    -controller : ElevatorController
    -isInternalRequest : boolean

    +ElevatorRequest(
        elevatorId : int,
        floor : int,
        isInternalRequest : boolean,
        direction : Direction
    )

    +execute()

    +getDirection() : Direction
    +getFloor() : int
    +checkIsInternalRequest() : boolean
}

ElevatorCommand <|.. ElevatorRequest

ElevatorRequest --> ElevatorController : uses
ElevatorRequest --> Direction


' =====================================================
' STRATEGY PATTERN
' =====================================================

interface SchedulingStrategy {

    +getNextStop(
        elevator : Elevator
    ) : int
}

class FCFSSchedulingStrategy {

    +getNextStop(
        elevator : Elevator
    ) : int
}

class ScanSchedulingStrategy {

    +getNextStop(
        elevator : Elevator
    ) : int

    -switchDirection(
        elevator : Elevator,
        requestsQueue
    ) : int
}

class LookSchedulingStrategy {

    +getNextStop(
        elevator : Elevator
    ) : int
}

SchedulingStrategy <|.. FCFSSchedulingStrategy
SchedulingStrategy <|.. ScanSchedulingStrategy
SchedulingStrategy <|.. LookSchedulingStrategy


' =====================================================
' ELEVATOR
' =====================================================

class Elevator {

    -id : int
    -currentFloor : int
    -direction : Direction
    -state : ElevatorState
    -observers : List<ElevatorObserver>
    -requests : Queue<ElevatorRequest>

    +Elevator(id : int)

    +addObserver(
        observer : ElevatorObserver
    )

    +removeObserver(
        observer : ElevatorObserver
    )

    +setState(
        newState : ElevatorState
    )

    +setDirection(
        newDirection : Direction
    )

    +addRequest(
        elevatorRequest : ElevatorRequest
    )

    +moveToNextStop(
        nextStop : int
    )

    +getId() : int
    +getCurrentFloor() : int
    +getDirection() : Direction
    +getState() : ElevatorState

    +getRequestsQueue() : Queue<ElevatorRequest>

    +getDestinationFloors() : List<ElevatorRequest>
}

Elevator --> Direction
Elevator --> ElevatorState
Elevator --> ElevatorObserver : notifies
Elevator --> ElevatorRequest : contains


' =====================================================
' ELEVATOR CONTROLLER
' =====================================================

class ElevatorController {

    -elevators : List<Elevator>
    -floors : List<Floor>
    -schedulingStrategy : SchedulingStrategy
    -currentElevatorId : int

    +ElevatorController(
        numberOfElevators : int,
        numberOfFloors : int
    )

    +setSchedulingStrategy(
        strategy : SchedulingStrategy
    )

    +requestElevator(
        elevatorId : int,
        floorNumber : int,
        direction : Direction
    )

    +requestFloor(
        elevatorId : int,
        floorNumber : int
    )

    +step()

    +getElevators() : List<Elevator>
    +getFloors() : List<Floor>

    +setCurrentElevator(
        elevatorId : int
    )

    -getElevatorById(
        elevatorId : int
    ) : Elevator
}

ElevatorController *-- "1..*" Elevator
ElevatorController *-- "1..*" Floor

ElevatorController --> SchedulingStrategy : uses
ElevatorController --> ElevatorRequest : creates


' =====================================================
' FLOOR
' =====================================================

class Floor {

    -floorNumber : int

    +Floor(
        floorNumber : int
    )

    +getFloorNumber() : int
}


' =====================================================
' BUILDING
' =====================================================

class Building {

    -name : String
    -numberOfFloors : int
    -elevatorController : ElevatorController

    +Building(
        name : String,
        numberOfFloors : int,
        numberOfElevators : int
    )

    +getName() : String
    +getNumberOfFloors() : int
    +getElevatorController() : ElevatorController
}

Building *-- ElevatorController


' =====================================================
' ELEVATOR FACTORY
' =====================================================

class ElevatorFactory {

    +createElevator(
        type : String,
        id : int
    ) : Elevator
}


class ExpressElevator {

    -SPEED_MULTIPLIER : int

    +ExpressElevator(
        id : int
    )

    +moveToNextStop(
        nextStop : int
    )
}

Elevator <|-- ExpressElevator

ElevatorFactory ..> Elevator : creates
ElevatorFactory ..> ExpressElevator : creates


' =====================================================
' MAIN
' =====================================================

class Main {

    +main(
        args : String[]
    )

    -displayElevatorStatus(
        elevators : List<Elevator>
    )
}

Main ..> Building
Main ..> ElevatorController
Main ..> ElevatorDisplay
Main ..> SchedulingStrategy
Main ..> ScanSchedulingStrategy
Main ..> FCFSSchedulingStrategy
Main ..> LookSchedulingStrategy


@enduml

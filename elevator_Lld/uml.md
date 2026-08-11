# Elevator System UML

```mermaid
classDiagram

%% ====================
%% ENUMS
%% ====================

class Direction {
    <<enumeration>>
    UP
    DOWN
    IDLE
}

class ElevatorState {
    <<enumeration>>
    IDLE
    MOVING
    STOPPED
    MAINTENANCE
}

%% ====================
%% OBSERVER PATTERN
%% ====================

class ElevatorObserver {
    <<interface>>
    +onElevatorStateChange(elevator, state)
    +onElevatorFloorChange(elevator, floor)
}

class ElevatorDisplay {
    +onElevatorStateChange(elevator, state)
    +onElevatorFloorChange(elevator, floor)
}

ElevatorObserver <|.. ElevatorDisplay

%% ====================
%% COMMAND PATTERN
%% ====================

class ElevatorCommand {
    <<interface>>
    +execute()
}

class ElevatorRequest {
    -elevatorId : int
    -floor : int
    -requestDirection : Direction
    -controller : ElevatorController
    -isInternalRequest : boolean

    +execute()
    +getDirection()
    +getFloor()
    +checkIsInternalRequest()
}

ElevatorCommand <|.. ElevatorRequest
ElevatorRequest --> ElevatorController
ElevatorRequest --> Direction

%% ====================
%% STRATEGY PATTERN
%% ====================

class SchedulingStrategy {
    <<interface>>
    +getNextStop(elevator)
}

class FCFSSchedulingStrategy {
    +getNextStop(elevator)
}

class ScanSchedulingStrategy {
    +getNextStop(elevator)
}

class LookSchedulingStrategy {
    +getNextStop(elevator)
}

SchedulingStrategy <|.. FCFSSchedulingStrategy
SchedulingStrategy <|.. ScanSchedulingStrategy
SchedulingStrategy <|.. LookSchedulingStrategy

SchedulingStrategy --> Elevator

%% ====================
%% ELEVATOR
%% ====================

class Elevator {
    -id : int
    -currentFloor : int
    -direction : Direction
    -state : ElevatorState
    -observers : List~ElevatorObserver~
    -requests : Queue~ElevatorRequest~

    +addObserver(observer)
    +removeObserver(observer)
    +setState(state)
    +setDirection(direction)
    +addRequest(request)
    +moveToNextStop(nextStop)
    +getId()
    +getCurrentFloor()
    +getDirection()
    +getState()
    +getRequestsQueue()
    +getDestinationFloors()
}

Elevator --> Direction
Elevator --> ElevatorState
Elevator --> ElevatorObserver
Elevator o-- ElevatorRequest

%% ====================
%% SPECIALIZED ELEVATOR
%% ====================

class ExpressElevator {
    -SPEED_MULTIPLIER : int
    +moveToNextStop(nextStop)
}

Elevator <|-- ExpressElevator

%% ====================
%% FLOOR
%% ====================

class Floor {
    -floorNumber : int

    +getFloorNumber()
}

%% ====================
%% CONTROLLER
%% ====================

class ElevatorController {
    -elevators : List~Elevator~
    -floors : List~Floor~
    -schedulingStrategy : SchedulingStrategy
    -currentElevatorId : int

    +setSchedulingStrategy(strategy)
    +requestElevator(elevatorId, floorNumber, direction)
    +requestFloor(elevatorId, floorNumber)
    +step()
    +getElevators()
    +getFloors()
    +setCurrentElevator(elevatorId)
    +addElevator(type)
    +replaceElevator(elevatorId, newType)
}

ElevatorController *-- Elevator
ElevatorController *-- Floor
ElevatorController --> SchedulingStrategy
ElevatorController ..> ElevatorRequest

%% ====================
%% FACTORY PATTERN
%% ====================

class ElevatorFactory {
    +createElevator(type, id)
}

ElevatorFactory ..> Elevator
ElevatorFactory ..> ExpressElevator

%% ====================
%% BUILDING
%% ====================

class Building {
    -name : String
    -numberOfFloors : int
    -elevatorController : ElevatorController

    +getName()
    +getNumberOfFloors()
    +getElevatorController()
}

Building *-- ElevatorController

%% ====================
%% MAIN
%% ====================

class Main

Main ..> Building
Main ..> ElevatorController
Main ..> ElevatorDisplay
Main ..> SchedulingStrategy
Main ..> ScanSchedulingStrategy
Main ..> FCFSSchedulingStrategy
```

## Design Patterns Used

### Strategy Pattern

* SchedulingStrategy
* FCFSSchedulingStrategy
* ScanSchedulingStrategy
* LookSchedulingStrategy

The scheduling algorithm is separated from the elevator controller.

Different scheduling algorithms can be selected dynamically:

* FCFS
* SCAN
* LOOK

This allows the elevator scheduling behavior to change without modifying the `ElevatorController`.

---

### Observer Pattern

* ElevatorObserver
* ElevatorDisplay

The `Elevator` notifies registered observers when:

* Its state changes
* Its current floor changes

```text
Elevator
    |
    +---- ElevatorObserver
                |
                +---- ElevatorDisplay
```

This allows additional observers such as logging systems, monitoring dashboards, or notification services to be added later.

---

### Command Pattern

* ElevatorCommand
* ElevatorRequest

`ElevatorRequest` encapsulates an elevator request as a command.

It supports two types of requests:

* External request — a passenger requests an elevator from a floor.
* Internal request — a passenger selects a destination floor inside an elevator.

```text
ElevatorRequest
       |
       v
ElevatorController
       |
       +---- requestElevator()
       |
       +---- requestFloor()
```

---

### Factory Pattern

* ElevatorFactory

The factory creates different elevator implementations based on the requested type.

Currently supported types include:

* Standard Elevator
* Express Elevator

```text
ElevatorFactory
      |
      +---- Elevator
      |
      +---- ExpressElevator
```

This makes it easier to add new elevator types without changing the client code.

---

## Composition

### Building → ElevatorController

A `Building` owns its `ElevatorController`.

```text
Building
    |
    +---- ElevatorController
```

### ElevatorController → Elevator

The controller manages the elevators in the building.

```text
ElevatorController
    |
    +---- Elevator
    |
    +---- Elevator
    |
    +---- Elevator
```

### ElevatorController → Floor

The controller maintains the floors available in the building.

```text
ElevatorController
    |
    +---- Floor
    |
    +---- Floor
    |
    +---- Floor
```

---

## Relationships

### Elevator → ElevatorObserver

The elevator maintains a list of observers and notifies them about state and floor changes.

### Elevator → ElevatorRequest

The elevator maintains a queue of pending requests.

### ElevatorController → SchedulingStrategy

The controller delegates the decision of the next elevator stop to the selected scheduling strategy.

### ElevatorRequest → ElevatorController

An elevator request delegates its execution to the controller.

### ElevatorController → Elevator

The controller manages and operates all elevators.

### ElevatorController → Floor

The controller maintains the floors of the building.

### Elevator → Direction

Each elevator has a current direction:

* UP
* DOWN
* IDLE

### Elevator → ElevatorState

Each elevator has an operational state:

* IDLE
* MOVING
* STOPPED
* MAINTENANCE

### ExpressElevator → Elevator

`ExpressElevator` extends the base `Elevator` and overrides movement behavior.

---

## Flow

```text
                    Building
                       |
                       v
              ElevatorController
                 /      |       \
                /       |        \
               v        v         v
          Elevators    Floors   SchedulingStrategy
              |                    |
              |                    +---- FCFS
              |                    +---- SCAN
              |                    +---- LOOK
              |
              +---- ElevatorRequest
              |
              +---- ElevatorObserver
                           |
                           +---- ElevatorDisplay
```

## Request Flow

```text
User
 |
 +---- External Request
 |          |
 |          v
 |   ElevatorRequest
 |          |
 |          v
 |   ElevatorController
 |          |
 |          v
 |      Elevator
 |
 +---- Internal Request
            |
            v
      ElevatorRequest
            |
            v
     ElevatorController
            |
            v
         Elevator
```

## Scheduling Flow

```text
ElevatorController
        |
        v
SchedulingStrategy
        |
        +---- FCFS
        |
        +---- SCAN
        |
        +---- LOOK
        |
        v
    Next Stop
        |
        v
     Elevator
        |
        v
  moveToNextStop()
```

## Elevator Type Creation Flow

```text
ElevatorController
        |
        v
 ElevatorFactory
        |
        +---- Standard Elevator
        |
        +---- ExpressElevator
        |
        v
     Elevator
```

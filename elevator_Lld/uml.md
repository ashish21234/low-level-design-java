
So for the **Elevator LLD**, use Mermaid `classDiagram`, not `@startuml`.

Here is the `uml.md` in the same format as your Tic Tac Toe one:

```markdown
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
    -switchDirection(elevator, requestsQueue)
}

class LookSchedulingStrategy {
    +getNextStop(elevator)
}

SchedulingStrategy <|.. FCFSSchedulingStrategy
SchedulingStrategy <|.. ScanSchedulingStrategy
SchedulingStrategy <|.. LookSchedulingStrategy

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

    +Elevator(id)
    +addObserver(observer)
    +removeObserver(observer)
    +setState(newState)
    +setDirection(newDirection)
    +addRequest(elevatorRequest)
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
Elevator --> ElevatorRequest

%% ====================
%% FLOOR
%% ====================

class Floor {
    -floorNumber : int

    +Floor(floorNumber)
    +getFloorNumber()
}

%% ====================
%% ELEVATOR CONTROLLER
%% ====================

class ElevatorController {
    -elevators : List~Elevator~
    -floors : List~Floor~
    -schedulingStrategy : SchedulingStrategy
    -currentElevatorId : int

    +ElevatorController(numberOfElevators, numberOfFloors)
    +setSchedulingStrategy(strategy)
    +requestElevator(elevatorId, floorNumber, direction)
    +requestFloor(elevatorId, floorNumber)
    +step()
    +getElevators()
    +getFloors()
    +setCurrentElevator(elevatorId)
    -getElevatorById(elevatorId)
}

ElevatorController *-- Elevator
ElevatorController *-- Floor
ElevatorController --> SchedulingStrategy
ElevatorController --> ElevatorRequest

%% ====================
%% BUILDING
%% ====================

class Building {
    -name : String
    -numberOfFloors : int
    -elevatorController : ElevatorController

    +Building(name, numberOfFloors, numberOfElevators)
    +getName()
    +getNumberOfFloors()
    +getElevatorController()
}

Building *-- ElevatorController

%% ====================
%% ELEVATOR FACTORY
%% ====================

class ElevatorFactory {
    +createElevator(type, id)
}

class ExpressElevator {
    -SPEED_MULTIPLIER : int

    +ExpressElevator(id)
    +moveToNextStop(nextStop)
}

Elevator <|-- ExpressElevator

ElevatorFactory ..> Elevator
ElevatorFactory ..> ExpressElevator

%% ====================
%% MAIN
%% ====================

class Main {
    +main(args)
    -displayElevatorStatus(elevators)
}

Main ..> Building
Main ..> ElevatorController
Main ..> ElevatorDisplay
Main ..> SchedulingStrategy
Main ..> ScanSchedulingStrategy
Main ..> FCFSSchedulingStrategy
Main ..> LookSchedulingStrategy

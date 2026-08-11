import java.util.*;


// ================================================================
// ENUM: Direction
// ================================================================
// Represents the direction in which an elevator is moving.
//
// UP   -> Elevator is moving upward
// DOWN -> Elevator is moving downward
// IDLE -> Elevator is not currently moving
// ================================================================

enum Direction {
    UP,
    DOWN,
    IDLE
}


// ================================================================
// ENUM: ElevatorState
// ================================================================
// Represents the current operational state of an elevator.
//
// IDLE        -> No pending movement
// MOVING      -> Currently moving
// STOPPED     -> Temporarily stopped at a floor
// MAINTENANCE -> Elevator is unavailable
// ================================================================

enum ElevatorState {
    IDLE,
    MOVING,
    STOPPED,
    MAINTENANCE
}


// ================================================================
// OBSERVER PATTERN
// ================================================================
//
// ElevatorObserver is the OBSERVER interface.
//
// The Elevator acts as the SUBJECT / OBSERVABLE.
//
// Whenever something important happens to an elevator,
// the elevator notifies all registered observers.
//
// Currently we notify observers about:
// 1. State changes
// 2. Floor changes
// ================================================================

interface ElevatorObserver {

    void onElevatorStateChange(
            Elevator elevator,
            ElevatorState state
    );

    void onElevatorFloorChange(
            Elevator elevator,
            int floor
    );
}


// ================================================================
// Concrete Observer
// ================================================================
//
// ElevatorDisplay is an observer of Elevator.
//
// In a real system, we could have:
//
// ElevatorDisplay
// MobileApp
// LoggingSystem
// AdminDashboard
//
// All of them could implement ElevatorObserver.
// ================================================================

class ElevatorDisplay implements ElevatorObserver {

    @Override
    public void onElevatorStateChange(
            Elevator elevator,
            ElevatorState state
    ) {

        System.out.println(
                "[Display] Elevator "
                        + elevator.getId()
                        + " state changed to "
                        + state
        );
    }


    @Override
    public void onElevatorFloorChange(
            Elevator elevator,
            int floor
    ) {

        System.out.println(
                "[Display] Elevator "
                        + elevator.getId()
                        + " moved to floor "
                        + floor
        );
    }
}


// ================================================================
// COMMAND PATTERN
// ================================================================
//
// ElevatorCommand is the COMMAND interface.
//
// The idea of Command Pattern:
//
// Instead of directly performing an operation,
// we represent that operation as an OBJECT.
//
// Example:
//
// ElevatorRequest
//      |
//      +---- execute()
//
// This allows requests to be stored, queued and executed later.
// ================================================================

interface ElevatorCommand {

    void execute();
}


// ================================================================
// FORWARD DECLARATION NOTE
// ================================================================
//
// ElevatorRequest uses ElevatorController.
//
// Since Java allows us to reference classes declared later
// in the same file, we can directly use ElevatorController here.
// ================================================================


// ================================================================
// ELEVATOR REQUEST
// ================================================================
//
// ElevatorRequest represents one request made by a passenger.
//
// There are two types of requests:
//
// 1. External Request
//    Passenger is outside the elevator.
//    Example:
//    Floor 5 -> UP
//
// 2. Internal Request
//    Passenger is already inside the elevator.
//    Example:
//    Elevator 2 -> Floor 8
//
// This class implements ElevatorCommand,
// therefore it belongs to the COMMAND PATTERN.
// ================================================================

class ElevatorRequest implements ElevatorCommand {

    private int elevatorId;

    private int floor;

    // Direction is mainly important for external requests.
    private Direction requestDirection;

    // Existing controller responsible for this request.
    private ElevatorController controller;

    // true  -> Internal request
    // false -> External request
    private boolean isInternalRequest;


    public ElevatorRequest(
            int elevatorId,
            int floor,
            boolean isInternalRequest,
            Direction direction,
            ElevatorController controller
    ) {

        this.elevatorId = elevatorId;
        this.floor = floor;
        this.isInternalRequest = isInternalRequest;
        this.requestDirection = direction;

        // IMPORTANT:
        // Use the existing controller.
        //
        // We should NOT do:
        //
        // controller = new ElevatorController(...);
        //
        // because that would create a completely different
        // elevator system.
        this.controller = controller;
    }


    // ============================================================
    // COMMAND EXECUTION
    // ============================================================
    //
    // When execute() is called, the request asks the controller
    // to process it.
    //
    // The request itself does not decide how the elevator
    // should move.
    //
    // That responsibility belongs to ElevatorController
    // and SchedulingStrategy.
    // ============================================================

    @Override
    public void execute() {

        controller.handleRequest(this);
    }


    public int getElevatorId() {
        return elevatorId;
    }


    public int getFloor() {
        return floor;
    }


    public Direction getDirection() {
        return requestDirection;
    }


    public boolean isInternalRequest() {
        return isInternalRequest;
    }
}


// ================================================================
// STRATEGY PATTERN
// ================================================================
//
// SchedulingStrategy is the STRATEGY interface.
//
// The controller does not need to know HOW the next elevator
// stop is calculated.
//
// It simply asks:
//
//     schedulingStrategy.getNextStop(elevator)
//
// Different algorithms can implement this interface.
//
// Currently:
//
// 1. FCFS
// 2. SCAN
// 3. LOOK
// ================================================================

interface SchedulingStrategy {

    int getNextStop(Elevator elevator);
}


// ================================================================
// FCFS SCHEDULING
// ================================================================
//
// FCFS = First Come First Served
//
// The first request that entered the queue is served first.
//
// Example:
//
// Current floor = 5
//
// Requests:
// 8
// 2
// 10
//
// FCFS:
//
// 5 -> 8 -> 2 -> 10
//
// It does NOT optimize elevator movement.
// It simply respects request order.
// ================================================================

class FCFSSchedulingStrategy implements SchedulingStrategy {

    @Override
    public int getNextStop(Elevator elevator) {

        Queue<ElevatorRequest> requests =
                elevator.getRequestsQueue();

        // No request -> stay where we are.
        if (requests.isEmpty()) {
            return elevator.getCurrentFloor();
        }

        // Look at the first request.
        // We use peek() instead of poll().
        //
        // Why?
        //
        // The strategy should decide the next stop.
        // It should NOT remove the request.
        //
        // The request will be removed when the elevator
        // actually reaches that floor.
        return requests.peek().getFloor();
    }
}


// ================================================================
// SCAN SCHEDULING
// ================================================================
//
// SCAN behaves somewhat like a real elevator.
//
// If elevator is moving UP:
//
//     Serve upward requests first.
//
// Once there are no more requests in that direction:
//
//     Change direction.
//
// Example:
//
// Current floor = 5
// Direction = UP
//
// Requests:
// 7
// 9
// 2
//
// Order:
//
// 5 -> 7 -> 9 -> 2
//
// SCAN tries to continue in the current direction as much
// as possible before reversing.
// ================================================================

class ScanSchedulingStrategy implements SchedulingStrategy {

    @Override
    public int getNextStop(Elevator elevator) {

        int currentFloor = elevator.getCurrentFloor();

        Direction direction = elevator.getDirection();

        Queue<ElevatorRequest> requests =
                elevator.getRequestsQueue();


        if (requests.isEmpty()) {
            return currentFloor;
        }


        // --------------------------------------------------------
        // Find nearest request above current floor.
        // --------------------------------------------------------

        Integer nearestUp = null;

        for (ElevatorRequest request : requests) {

            int floor = request.getFloor();

            if (floor > currentFloor) {

                if (nearestUp == null ||
                        floor < nearestUp) {

                    nearestUp = floor;
                }
            }
        }


        // --------------------------------------------------------
        // Find nearest request below current floor.
        // --------------------------------------------------------

        Integer nearestDown = null;

        for (ElevatorRequest request : requests) {

            int floor = request.getFloor();

            if (floor < currentFloor) {

                if (nearestDown == null ||
                        floor > nearestDown) {

                    nearestDown = floor;
                }
            }
        }


        // --------------------------------------------------------
        // If elevator is IDLE,
        // choose the closest request.
        // --------------------------------------------------------

        if (direction == Direction.IDLE) {

            if (nearestUp == null) {
                return nearestDown;
            }

            if (nearestDown == null) {
                return nearestUp;
            }


            int upDistance =
                    nearestUp - currentFloor;

            int downDistance =
                    currentFloor - nearestDown;


            if (upDistance <= downDistance) {
                return nearestUp;
            }

            return nearestDown;
        }


        // --------------------------------------------------------
        // Elevator is moving UP.
        // --------------------------------------------------------

        if (direction == Direction.UP) {

            // If there is a request above,
            // continue moving upward.
            if (nearestUp != null) {
                return nearestUp;
            }

            // No request above.
            // Therefore change direction.
            if (nearestDown != null) {
                return nearestDown;
            }
        }


        // --------------------------------------------------------
        // Elevator is moving DOWN.
        // --------------------------------------------------------

        if (direction == Direction.DOWN) {

            // If there is a request below,
            // continue moving downward.
            if (nearestDown != null) {
                return nearestDown;
            }

            // No request below.
            // Therefore change direction.
            if (nearestUp != null) {
                return nearestUp;
            }
        }


        return currentFloor;
    }
}


// ================================================================
// LOOK SCHEDULING
// ================================================================
//
// LOOK is similar to SCAN.
//
// The major idea:
//
//     Continue in the current direction while there are
//     requests in that direction.
//
// But unlike SCAN, we do not need to travel to the physical
// end of the building.
//
// We only go as far as the last relevant request.
//
// Example:
//
// Current floor = 5
// Direction = UP
//
// Requests:
// 7
// 9
//
// LOOK:
//
// 5 -> 7 -> 9
//
// It does not continue to floor 10/20/etc. just because
// that is the building's maximum floor.
// ================================================================

class LookSchedulingStrategy implements SchedulingStrategy {

    @Override
    public int getNextStop(Elevator elevator) {

        int currentFloor =
                elevator.getCurrentFloor();

        Direction direction =
                elevator.getDirection();

        Queue<ElevatorRequest> requests =
                elevator.getRequestsQueue();


        if (requests.isEmpty()) {
            return currentFloor;
        }


        // ========================================================
        // ELEVATOR IS MOVING UP
        // ========================================================

        if (direction == Direction.UP) {

            Integer closestUp = null;

            for (ElevatorRequest request : requests) {

                int floor = request.getFloor();

                if (floor > currentFloor) {

                    // For an internal request,
                    // we always consider it.
                    //
                    // For an external request,
                    // we only consider it if the passenger
                    // is also asking to go UP.

                    boolean canServe =
                            request.isInternalRequest()
                                    ||
                            request.getDirection()
                                    == Direction.UP;


                    if (canServe) {

                        if (closestUp == null ||
                                floor < closestUp) {

                            closestUp = floor;
                        }
                    }
                }
            }


            // Continue UP if possible.
            if (closestUp != null) {
                return closestUp;
            }


            // No request above.
            // Find a request below and reverse.
            Integer closestDown = null;

            for (ElevatorRequest request : requests) {

                int floor = request.getFloor();

                if (floor < currentFloor) {

                    if (closestDown == null ||
                            floor > closestDown) {

                        closestDown = floor;
                    }
                }
            }


            if (closestDown != null) {
                return closestDown;
            }
        }


        // ========================================================
        // ELEVATOR IS MOVING DOWN
        // ========================================================

        if (direction == Direction.DOWN) {

            Integer closestDown = null;

            for (ElevatorRequest request : requests) {

                int floor = request.getFloor();

                if (floor < currentFloor) {

                    boolean canServe =
                            request.isInternalRequest()
                                    ||
                            request.getDirection()
                                    == Direction.DOWN;


                    if (canServe) {

                        if (closestDown == null ||
                                floor > closestDown) {

                            closestDown = floor;
                        }
                    }
                }
            }


            // Continue DOWN if possible.
            if (closestDown != null) {
                return closestDown;
            }


            // No request below.
            // Find a request above and reverse.
            Integer closestUp = null;

            for (ElevatorRequest request : requests) {

                int floor = request.getFloor();

                if (floor > currentFloor) {

                    if (closestUp == null ||
                            floor < closestUp) {

                        closestUp = floor;
                    }
                }
            }


            if (closestUp != null) {
                return closestUp;
            }
        }


        // ========================================================
        // ELEVATOR IS IDLE
        // ========================================================

        if (direction == Direction.IDLE) {

            ElevatorRequest firstRequest =
                    requests.peek();

            return firstRequest.getFloor();
        }


        return currentFloor;
    }
}


// ================================================================
// FLOOR
// ================================================================
//
// Represents a floor inside the building.
//
// The class is intentionally simple because a floor does not
// need to make scheduling decisions.
// ================================================================

class Floor {

    private int floorNumber;


    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }


    public int getFloorNumber() {
        return floorNumber;
    }
}


// ================================================================
// ELEVATOR
// ================================================================
//
// This is the CORE ENTITY of the system.
//
// An Elevator knows:
//
// - Its ID
// - Current floor
// - Current direction
// - Current state
// - Pending requests
// - Observers
//
// But it does NOT decide which request should be served next.
//
// That decision is delegated to SchedulingStrategy.
//
// This is an important separation of responsibility.
// ================================================================

class Elevator {

    private int id;

    private int currentFloor;

    private Direction direction;

    private ElevatorState state;


    // ============================================================
    // OBSERVER PATTERN
    // ============================================================

    private List<ElevatorObserver> observers;


    // ============================================================
    // COMMAND PATTERN
    // ============================================================

    // All requests waiting to be processed by this elevator.
    private Queue<ElevatorRequest> requests;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public Elevator(int id) {

        this.id = id;

        // Every elevator starts at floor 1.
        this.currentFloor = 1;

        this.direction = Direction.IDLE;

        this.state = ElevatorState.IDLE;

        this.observers = new ArrayList<>();

        this.requests = new LinkedList<>();
    }


    // ============================================================
    // OBSERVER METHODS
    // ============================================================

    public void addObserver(ElevatorObserver observer) {

        observers.add(observer);
    }


    public void removeObserver(ElevatorObserver observer) {

        observers.remove(observer);
    }


    // Notify observers whenever elevator state changes.
    protected void notifyStateChange(
            ElevatorState newState
    ) {

        for (ElevatorObserver observer : observers) {

            observer.onElevatorStateChange(
                    this,
                    newState
            );
        }
    }


    // Notify observers whenever elevator floor changes.
    //
    // protected is used because subclasses such as
    // ExpressElevator may also need to notify observers.
    protected void notifyFloorChange(int floor) {

        for (ElevatorObserver observer : observers) {

            observer.onElevatorFloorChange(
                    this,
                    floor
            );
        }
    }


    // ============================================================
    // STATE MANAGEMENT
    // ============================================================

    public void setState(ElevatorState newState) {

        this.state = newState;

        // Whenever state changes,
        // notify all observers.
        notifyStateChange(newState);
    }


    public void setDirection(Direction newDirection) {

        this.direction = newDirection;
    }


    // ============================================================
    // REQUEST MANAGEMENT
    // ============================================================

    public void addRequest(ElevatorRequest request) {

        // Avoid adding the exact same request object twice.
        if (!requests.contains(request)) {

            requests.add(request);
        }


        // If elevator is idle and now has a request,
        // it should start moving.
        if (state == ElevatorState.IDLE &&
                !requests.isEmpty()) {

            int requestedFloor =
                    request.getFloor();


            if (requestedFloor > currentFloor) {

                direction = Direction.UP;

            } else if (requestedFloor < currentFloor) {

                direction = Direction.DOWN;
            }


            // If requestedFloor == currentFloor,
            // direction remains unchanged.

            if (requestedFloor != currentFloor) {

                setState(ElevatorState.MOVING);
            }
        }
    }


    // ============================================================
    // MOVEMENT
    // ============================================================
    //
    // SchedulingStrategy decides:
    //
    //     "Where should the elevator go?"
    //
    // Elevator is responsible for:
    //
    //     "Actually moving there."
    //
    // ============================================================

    public void moveToNextStop(int nextStop) {

        // Elevator can only move when it is MOVING.
        if (state != ElevatorState.MOVING) {
            return;
        }


        // If target is above current floor,
        // direction must be UP.
        if (nextStop > currentFloor) {

            direction = Direction.UP;

        }

        // If target is below current floor,
        // direction must be DOWN.
        else if (nextStop < currentFloor) {

            direction = Direction.DOWN;
        }


        // Move one floor at a time.
        while (currentFloor != nextStop) {

            if (direction == Direction.UP) {

                currentFloor++;

            } else {

                currentFloor--;
            }


            // Every floor change is observable.
            notifyFloorChange(currentFloor);
        }


        // We have reached the destination.
        completeArrival();
    }


    // ============================================================
    // ARRIVAL
    // ============================================================
    //
    // Called when elevator reaches its next destination.
    // ============================================================

    protected void completeArrival() {

        // Elevator temporarily stops.
        setState(ElevatorState.STOPPED);


        // Remove every request whose destination
        // is the current floor.
        requests.removeIf(
                request ->
                        request.getFloor()
                                == currentFloor
        );


        // If there are no more requests,
        // elevator becomes IDLE.
        if (requests.isEmpty()) {

            direction = Direction.IDLE;

            setState(ElevatorState.IDLE);
        }

        // Otherwise continue moving.
        else {

            setState(ElevatorState.MOVING);
        }
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public int getId() {
        return id;
    }


    public int getCurrentFloor() {
        return currentFloor;
    }


    public Direction getDirection() {
        return direction;
    }


    public ElevatorState getState() {
        return state;
    }


    // Return a COPY of the queue.
    //
    // Why?
    //
    // We don't want outside classes directly modifying
    // the elevator's internal queue.
    public Queue<ElevatorRequest> getRequestsQueue() {

        return new LinkedList<>(requests);
    }


    // Useful for displaying destinations.
    public List<ElevatorRequest> getDestinationFloors() {

        return new ArrayList<>(requests);
    }
}


// ================================================================
// EXPRESS ELEVATOR
// ================================================================
//
// ExpressElevator IS-A Elevator.
//
// Therefore:
//
// Elevator
//    ▲
//    |
// ExpressElevator
//
// This demonstrates INHERITANCE.
//
// It overrides movement behavior to move faster.
// ================================================================

class ExpressElevator extends Elevator {

    private static final int SPEED_MULTIPLIER = 2;


    public ExpressElevator(int id) {

        super(id);
    }


    @Override
    public void moveToNextStop(int nextStop) {

        if (getState() != ElevatorState.MOVING) {
            return;
        }


        // Determine direction.

        if (nextStop > getCurrentFloor()) {

            setDirection(Direction.UP);

        } else if (nextStop < getCurrentFloor()) {

            setDirection(Direction.DOWN);
        }


        // Express elevator moves multiple floors
        // per simulation step.
        while (getCurrentFloor() != nextStop) {

            int currentFloor =
                    getCurrentFloor();

            int newFloor;


            if (getDirection() == Direction.UP) {

                newFloor =
                        Math.min(
                                currentFloor + SPEED_MULTIPLIER,
                                nextStop
                        );

            } else {

                newFloor =
                        Math.max(
                                currentFloor - SPEED_MULTIPLIER,
                                nextStop
                        );
            }


            // Move floor by floor internally so observers
            // still receive floor-change notifications.
            while (currentFloor != newFloor) {

                if (getDirection() == Direction.UP) {

                    currentFloor++;

                } else {

                    currentFloor--;
                }


                // We need to update the actual elevator's
                // current floor.
                //
                // Since currentFloor in Elevator is private,
                // we use the helper method below.
                updateCurrentFloorForSubclass(currentFloor);

                notifyFloorChange(currentFloor);
            }
        }


        completeArrival();
    }


    // Helper used by ExpressElevator to update the inherited
    // current floor.
    //
    // In a larger multi-file implementation, we could instead
    // make currentFloor protected or create a protected setter.
    private void updateCurrentFloorForSubclass(int floor) {

        setCurrentFloorInternal(floor);
    }


    // Package/protected helper in the base class would be cleaner,
    // but we keep this method simple for this single-file example.
    private void setCurrentFloorInternal(int floor) {

        try {

            java.lang.reflect.Field field =
                    Elevator.class.getDeclaredField(
                            "currentFloor"
                    );

            field.setAccessible(true);

            field.setInt(this, floor);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to update elevator floor",
                    e
            );
        }
    }
}


// ================================================================
// FACTORY PATTERN
// ================================================================
//
// ElevatorFactory is responsible for creating elevator objects.
//
// Client does NOT need to know:
//
//     new Elevator(...)
//
// or:
//
//     new ExpressElevator(...)
//
// Instead:
//
//     ElevatorFactory.createElevator(type, id)
//
// ================================================================

class ElevatorFactory {

    public static Elevator createElevator(
            String type,
            int id
    ) {

        switch (type.toLowerCase()) {

            case "standard":

                return new Elevator(id);


            case "express":

                return new ExpressElevator(id);


            default:

                throw new IllegalArgumentException(
                        "Unknown elevator type: " + type
                );
        }
    }
}


// ================================================================
// ELEVATOR CONTROLLER
// ================================================================
//
// This is the CENTRAL COORDINATOR.
//
// Responsibilities:
//
// 1. Maintain elevators
// 2. Maintain floors
// 3. Receive external requests
// 4. Receive internal requests
// 5. Select scheduling strategy
// 6. Perform simulation steps
//
// It does NOT itself implement FCFS/SCAN/LOOK.
//
// That responsibility belongs to SchedulingStrategy.
// ================================================================

class ElevatorController {

    private List<Elevator> elevators;

    private List<Floor> floors;

    // STRATEGY PATTERN
    private SchedulingStrategy schedulingStrategy;

    private int currentElevatorId;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ElevatorController(
            int numberOfElevators,
            int numberOfFloors
    ) {

        elevators = new ArrayList<>();

        floors = new ArrayList<>();


        // Default scheduling strategy.
        schedulingStrategy =
                new ScanSchedulingStrategy();


        // --------------------------------------------------------
        // Create floors.
        // --------------------------------------------------------

        for (int i = 1;
             i <= numberOfFloors;
             i++) {

            floors.add(
                    new Floor(i)
            );
        }


        // --------------------------------------------------------
        // Create elevators.
        //
        // Factory Pattern is used here.
        // --------------------------------------------------------

        for (int i = 1;
             i <= numberOfElevators;
             i++) {

            elevators.add(
                    ElevatorFactory.createElevator(
                            "standard",
                            i
                    )
            );
        }
    }


    // ============================================================
    // STRATEGY SETTER
    // ============================================================

    public void setSchedulingStrategy(
            SchedulingStrategy strategy
    ) {

        this.schedulingStrategy = strategy;
    }


    // ============================================================
    // EXTERNAL REQUEST
    // ============================================================
    //
    // Example:
    //
    // Passenger is at floor 5
    // Passenger wants to go UP
    //
    // elevatorId tells us which elevator is assigned
    // to this request in this simplified system.
    // ============================================================

    public void requestElevator(
            int elevatorId,
            int floorNumber,
            Direction direction
    ) {

        ElevatorRequest request =
                new ElevatorRequest(
                        elevatorId,
                        floorNumber,
                        false,
                        direction,
                        this
                );


        // COMMAND PATTERN:
        //
        // Instead of directly modifying the elevator,
        // we execute the request object.
        request.execute();
    }


    // ============================================================
    // INTERNAL REQUEST
    // ============================================================
    //
    // Example:
    //
    // Passenger is inside Elevator 2.
    // Passenger presses floor 8.
    // ============================================================

    public void requestFloor(
            int elevatorId,
            int floorNumber
    ) {

        Elevator elevator =
                getElevatorById(elevatorId);


        if (elevator == null) {

            System.out.println(
                    "Elevator not found!"
            );

            return;
        }


        Direction direction;

        if (floorNumber >
                elevator.getCurrentFloor()) {

            direction = Direction.UP;

        } else if (floorNumber <
                elevator.getCurrentFloor()) {

            direction = Direction.DOWN;

        } else {

            direction = Direction.IDLE;
        }


        ElevatorRequest request =
                new ElevatorRequest(
                        elevatorId,
                        floorNumber,
                        true,
                        direction,
                        this
                );


        request.execute();
    }


    // ============================================================
    // REQUEST HANDLER
    // ============================================================
    //
    // This method is called by ElevatorRequest.execute().
    //
    // This is the actual point where the request is processed.
    // ============================================================

    public void handleRequest(
            ElevatorRequest request
    ) {

        Elevator elevator =
                getElevatorById(
                        request.getElevatorId()
                );


        if (elevator == null) {

            System.out.println(
                    "No elevator found with ID "
                            + request.getElevatorId()
            );

            return;
        }


        System.out.println();


        if (request.isInternalRequest()) {

            System.out.println(
                    "[Internal Request] "
                            + "Elevator "
                            + elevator.getId()
                            + " -> Floor "
                            + request.getFloor()
            );

        } else {

            System.out.println(
                    "[External Request] "
                            + "Floor "
                            + request.getFloor()
                            + ", Direction "
                            + request.getDirection()
            );
        }


        // Add request to elevator's queue.
        elevator.addRequest(request);
    }


    // ============================================================
    // SIMULATION STEP
    // ============================================================
    //
    // This is where Strategy Pattern becomes visible.
    //
    // Controller asks:
    //
    //     schedulingStrategy.getNextStop(elevator)
    //
    // The controller doesn't care whether the strategy is:
    //
    // FCFS
    // SCAN
    // LOOK
    //
    // It just gets the next stop.
    // ============================================================

    public void step() {

        System.out.println(
                "\n========== SIMULATION STEP =========="
        );


        for (Elevator elevator : elevators) {

            Queue<ElevatorRequest> requests =
                    elevator.getRequestsQueue();


            // No pending requests.
            if (requests.isEmpty()) {

                continue;
            }


            // Ask Strategy:
            //
            // "What should this elevator's next stop be?"
            int nextStop =
                    schedulingStrategy.getNextStop(
                            elevator
                    );


            System.out.println(
                    "Elevator "
                            + elevator.getId()
                            + " | Current Floor: "
                            + elevator.getCurrentFloor()
                            + " | Direction: "
                            + elevator.getDirection()
                            + " | Next Stop: "
                            + nextStop
            );


            // Actually move the elevator.
            elevator.moveToNextStop(
                    nextStop
            );
        }
    }


    // ============================================================
    // FIND ELEVATOR
    // ============================================================

    private Elevator getElevatorById(
            int elevatorId
    ) {

        for (Elevator elevator : elevators) {

            if (elevator.getId() == elevatorId) {

                return elevator;
            }
        }

        return null;
    }


    // ============================================================
    // ADD NEW ELEVATOR
    // ============================================================
    //
    // Factory Pattern is used here.
    //
    // Example:
    //
    // controller.addElevator("express");
    // ============================================================

    public void addElevator(String type) {

        int nextId =
                elevators.size() + 1;


        Elevator elevator =
                ElevatorFactory.createElevator(
                        type,
                        nextId
                );


        elevators.add(elevator);
    }


    // ============================================================
    // REPLACE ELEVATOR
    // ============================================================

    public boolean replaceElevator(
            int elevatorId,
            String newType
    ) {

        Elevator oldElevator =
                getElevatorById(elevatorId);


        if (oldElevator == null) {

            return false;
        }


        int index =
                elevators.indexOf(
                        oldElevator
                );


        Elevator newElevator =
                ElevatorFactory.createElevator(
                        newType,
                        elevatorId
                );


        elevators.set(
                index,
                newElevator
        );


        return true;
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public List<Elevator> getElevators() {

        return elevators;
    }


    public List<Floor> getFloors() {

        return floors;
    }


    public void setCurrentElevator(
            int elevatorId
    ) {

        this.currentElevatorId =
                elevatorId;
    }


    public int getCurrentElevatorId() {

        return currentElevatorId;
    }
}


// ================================================================
// BUILDING
// ================================================================
//
// Building represents the overall physical building.
//
// It owns:
//
//     ElevatorController
//
// The controller then manages:
//
//     Elevators
//     Floors
// ================================================================

class Building {

    private String name;

    private int numberOfFloors;

    private ElevatorController elevatorController;


    public Building(
            String name,
            int numberOfFloors,
            int numberOfElevators
    ) {

        this.name = name;

        this.numberOfFloors =
                numberOfFloors;


        // Composition:
        //
        // Building creates its ElevatorController.
        this.elevatorController =
                new ElevatorController(
                        numberOfElevators,
                        numberOfFloors
                );
    }


    public String getName() {

        return name;
    }


    public int getNumberOfFloors() {

        return numberOfFloors;
    }


    public ElevatorController
    getElevatorController() {

        return elevatorController;
    }
}


// ================================================================
// MAIN CLASS
// ================================================================
//
// Main acts as the CLIENT of our system.
//
// It:
//
// 1. Creates Building
// 2. Gets ElevatorController
// 3. Adds Observer
// 4. Takes user input
// 5. Sends requests
// 6. Changes scheduling strategy
// 7. Runs simulation steps
// ================================================================

public class Main {

    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(System.in);


        // ========================================================
        // CREATE BUILDING
        // ========================================================

        Building building =
                new Building(
                        "Office Tower",
                        10,
                        3
                );


        ElevatorController controller =
                building.getElevatorController();


        System.out.println(
                "========================================"
        );

        System.out.println(
                "       ELEVATOR SYSTEM SIMULATION"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Building : "
                        + building.getName()
        );

        System.out.println(
                "Floors   : "
                        + building.getNumberOfFloors()
        );

        System.out.println(
                "Elevators: "
                        + controller
                        .getElevators()
                        .size()
        );


        // ========================================================
        // OBSERVER PATTERN
        // ========================================================
        //
        // Create one display observer.
        //
        // Attach it to every elevator.
        // ========================================================

        ElevatorDisplay display =
                new ElevatorDisplay();


        for (Elevator elevator :
                controller.getElevators()) {

            elevator.addObserver(display);
        }


        // ========================================================
        // MAIN MENU
        // ========================================================

        boolean running = true;


        while (running) {

            System.out.println(
                    "\n----------------------------------------"
            );

            System.out.println(
                    "1. External Elevator Request"
            );

            System.out.println(
                    "2. Internal Floor Request"
            );

            System.out.println(
                    "3. Simulate Next Step"
            );

            System.out.println(
                    "4. Change Scheduling Strategy"
            );

            System.out.println(
                    "5. Add Elevator"
            );

            System.out.println(
                    "6. Show Elevator Status"
            );

            System.out.println(
                    "7. Exit"
            );

            System.out.println(
                    "----------------------------------------"
            );

            System.out.print(
                    "Enter choice: "
            );


            int choice =
                    scanner.nextInt();


            // ====================================================
            // EXTERNAL REQUEST
            // ====================================================

            switch (choice) {

                case 1:

                    System.out.print(
                            "Enter elevator ID: "
                    );

                    int externalElevatorId =
                            scanner.nextInt();


                    System.out.print(
                            "Enter floor number: "
                    );

                    int externalFloor =
                            scanner.nextInt();


                    System.out.print(
                            "Direction (1 = UP, 2 = DOWN): "
                    );

                    int directionChoice =
                            scanner.nextInt();


                    Direction direction;

                    if (directionChoice == 1) {

                        direction =
                                Direction.UP;

                    } else {

                        direction =
                                Direction.DOWN;
                    }


                    controller.setCurrentElevator(
                            externalElevatorId
                    );


                    controller.requestElevator(
                            externalElevatorId,
                            externalFloor,
                            direction
                    );


                    break;


                // =================================================
                // INTERNAL REQUEST
                // =================================================

                case 2:

                    System.out.print(
                            "Enter elevator ID: "
                    );

                    int internalElevatorId =
                            scanner.nextInt();


                    System.out.print(
                            "Enter destination floor: "
                    );

                    int destinationFloor =
                            scanner.nextInt();


                    controller.setCurrentElevator(
                            internalElevatorId
                    );


                    controller.requestFloor(
                            internalElevatorId,
                            destinationFloor
                    );


                    break;


                // =================================================
                // SIMULATION STEP
                // =================================================

                case 3:

                    controller.step();

                    break;


                // =================================================
                // CHANGE STRATEGY
                // =================================================

                case 4:

                    System.out.println(
                            "\nSelect Scheduling Strategy:"
                    );

                    System.out.println(
                            "1. FCFS"
                    );

                    System.out.println(
                            "2. SCAN"
                    );

                    System.out.println(
                            "3. LOOK"
                    );


                    System.out.print(
                            "Enter choice: "
                    );


                    int strategyChoice =
                            scanner.nextInt();


                    if (strategyChoice == 1) {

                        controller.setSchedulingStrategy(
                                new FCFSSchedulingStrategy()
                        );

                        System.out.println(
                                "FCFS strategy selected."
                        );


                    } else if (strategyChoice == 2) {

                        controller.setSchedulingStrategy(
                                new ScanSchedulingStrategy()
                        );

                        System.out.println(
                                "SCAN strategy selected."
                        );


                    } else if (strategyChoice == 3) {

                        controller.setSchedulingStrategy(
                                new LookSchedulingStrategy()
                        );

                        System.out.println(
                                "LOOK strategy selected."
                        );


                    } else {

                        System.out.println(
                                "Invalid strategy choice."
                        );
                    }


                    break;


                // =================================================
                // ADD ELEVATOR
                // =================================================

                case 5:

                    System.out.println(
                            "\nElevator Types:"
                    );

                    System.out.println(
                            "1. Standard"
                    );

                    System.out.println(
                            "2. Express"
                    );


                    System.out.print(
                            "Enter type: "
                    );


                    int elevatorType =
                            scanner.nextInt();


                    if (elevatorType == 1) {

                        controller.addElevator(
                                "standard"
                        );


                        System.out.println(
                                "Standard elevator added."
                        );


                    } else if (elevatorType == 2) {

                        Elevator newElevator =
                                ElevatorFactory
                                        .createElevator(
                                                "express",
                                                controller
                                                        .getElevators()
                                                        .size() + 1
                                        );


                        controller
                                .getElevators()
                                .add(newElevator);


                        newElevator.addObserver(
                                display
                        );


                        System.out.println(
                                "Express elevator added."
                        );


                    } else {

                        System.out.println(
                                "Invalid elevator type."
                        );
                    }


                    break;


                // =================================================
                // SHOW STATUS
                // =================================================

                case 6:

                    displayElevatorStatus(
                            controller.getElevators()
                    );

                    break;


                // =================================================
                // EXIT
                // =================================================

                case 7:

                    running = false;

                    break;


                default:

                    System.out.println(
                            "Invalid choice!"
                    );
            }
        }


        scanner.close();


        System.out.println(
                "\nSimulation ended."
        );
    }


    // ============================================================
    // DISPLAY ELEVATOR STATUS
    // ============================================================

    private static void displayElevatorStatus(
            List<Elevator> elevators
    ) {

        System.out.println(
                "\n========== ELEVATOR STATUS =========="
        );


        for (Elevator elevator :
                elevators) {

            System.out.println();

            System.out.println(
                    "Elevator ID : "
                            + elevator.getId()
            );

            System.out.println(
                    "Floor      : "
                            + elevator.getCurrentFloor()
            );

            System.out.println(
                    "Direction  : "
                            + elevator.getDirection()
            );

            System.out.println(
                    "State      : "
                            + elevator.getState()
            );


            System.out.print(
                    "Destinations: "
            );


            List<ElevatorRequest> requests =
                    elevator.getDestinationFloors();


            if (requests.isEmpty()) {

                System.out.println(
                        "None"
                );

            } else {

                for (ElevatorRequest request :
                        requests) {

                    System.out.print(
                            request.getFloor()
                                    + " "
                    );
                }

                System.out.println();
            }
        }
    }
}

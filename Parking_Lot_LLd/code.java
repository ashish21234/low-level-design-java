
import java.util.*;

// ====================
// FEE STRATEGY
// ====================

interface ParkingFeeStrategy {
    double calculateFee(String vehicleType, int duration, DurationType durationType);
}

enum DurationType {
    HOURS, DAYS
}

class BasicHourlyRateStrategy implements ParkingFeeStrategy {
    public double calculateFee(String vehicleType, int duration, DurationType durationType) {
        double rate;
        switch (vehicleType.toLowerCase()) {
            case "car": rate = 10; break;
            case "bike": rate = 5; break;
            default: rate = 8;
        }
        return durationType == DurationType.HOURS ? duration * rate : duration * rate * 24;
    }
}

class PremiumRateStrategy implements ParkingFeeStrategy {
    public double calculateFee(String vehicleType, int duration, DurationType durationType) {
        double rate;
        switch (vehicleType.toLowerCase()) {
            case "car": rate = 15; break;
            case "bike": rate = 8; break;
            default: rate = 12;
        }
        return durationType == DurationType.HOURS ? duration * rate : duration * rate * 24;
    }
}

// ====================
// VEHICLES
// ====================

abstract class Vehicle {
    private String licensePlate;
    private String vehicleType;
    private ParkingFeeStrategy feeStrategy;

    public Vehicle(String licensePlate, String vehicleType, ParkingFeeStrategy feeStrategy) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.feeStrategy = feeStrategy;
    }

    public String getVehicleType() { return vehicleType; }
    public String getLicensePlate() { return licensePlate; }

    public double calculateFee(int duration, DurationType durationType) {
        return feeStrategy.calculateFee(vehicleType, duration, durationType);
    }
}

class CarVehicle extends Vehicle {
    public CarVehicle(String lp, String vt, ParkingFeeStrategy fs) { super(lp, vt, fs); }
}

class BikeVehicle extends Vehicle {
    public BikeVehicle(String lp, String vt, ParkingFeeStrategy fs) { super(lp, vt, fs); }
}

class OtherVehicle extends Vehicle {
    public OtherVehicle(String lp, String vt, ParkingFeeStrategy fs) { super(lp, vt, fs); }
}

class VehicleFactory {
    public static Vehicle createVehicle(String vehicleType, String licensePlate,
                                        ParkingFeeStrategy feeStrategy) {
        if (vehicleType.equalsIgnoreCase("car"))
            return new CarVehicle(licensePlate, vehicleType, feeStrategy);
        if (vehicleType.equalsIgnoreCase("bike"))
            return new BikeVehicle(licensePlate, vehicleType, feeStrategy);
        return new OtherVehicle(licensePlate, vehicleType, feeStrategy);
    }
}

// ====================
// PAYMENT STRATEGY
// ====================

interface PaymentStrategy {
    void processPayment(double amount);
}

class CashPayment implements PaymentStrategy {
    public void processPayment(double amount) {
        System.out.println("Cash Payment: $" + amount);
    }
}

class CreditCardPayment implements PaymentStrategy {
    public void processPayment(double amount) {
        System.out.println("Credit Card Payment: $" + amount);
    }
}

class Payment {
    private double amount;
    private PaymentStrategy strategy;

    public Payment(double amount, PaymentStrategy strategy) {
        this.amount = amount;
        this.strategy = strategy;
    }

    public void processPayment() {
        strategy.processPayment(amount);
    }
}

// ====================
// PARKING SPOTS
// ====================

abstract class ParkingSpot {
    private int spotNumber;
    private boolean occupied;
    private Vehicle vehicle;
    private String spotType;

    public ParkingSpot(int spotNumber, String spotType) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
    }

    public boolean isOccupied() { return occupied; }
    public int getSpotNumber() { return spotNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public String getSpotType() { return spotType; }

    public abstract boolean canParkVehicle(Vehicle vehicle);

    public void parkVehicle(Vehicle vehicle) {
        if (!canParkVehicle(vehicle))
            throw new IllegalArgumentException("Invalid vehicle for spot");
        this.vehicle = vehicle;
        this.occupied = true;
    }

    public void vacate() {
        vehicle = null;
        occupied = false;
    }
}

class CarParkingSpot extends ParkingSpot {
    public CarParkingSpot(int no, String type) { super(no, type); }

    public boolean canParkVehicle(Vehicle vehicle) {
        return vehicle.getVehicleType().equalsIgnoreCase("car");
    }
}

class BikeParkingSpot extends ParkingSpot {
    public BikeParkingSpot(int no, String type) { super(no, type); }

    public boolean canParkVehicle(Vehicle vehicle) {
        return vehicle.getVehicleType().equalsIgnoreCase("bike");
    }
}

// ====================
// PARKING LOT
// ====================

class ParkingLot {
    private List<ParkingSpot> spots;

    public ParkingLot(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    public ParkingSpot findAvailableSpot(String vehicleType) {
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied() &&
                spot.getSpotType().equalsIgnoreCase(vehicleType)) {
                return spot;
            }
        }
        return null;
    }

    public ParkingSpot parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = findAvailableSpot(vehicle.getVehicleType());
        if (spot != null) {
            spot.parkVehicle(vehicle);
            System.out.println("Parked at spot " + spot.getSpotNumber());
        }
        return spot;
    }

    public void vacateSpot(ParkingSpot spot, Vehicle vehicle) {
        if (spot != null && spot.getVehicle() == vehicle) {
            spot.vacate();
            System.out.println("Spot " + spot.getSpotNumber() + " vacated");
        }
    }
}

// ====================
// MAIN
// ====================

public class ParkingLotSystem {

    private static PaymentStrategy getPaymentStrategy(int choice) {
        return choice == 2 ? new CashPayment() : new CreditCardPayment();
    }

    public static void main(String[] args) {

        List<ParkingSpot> spots = new ArrayList<>();
        spots.add(new CarParkingSpot(1, "Car"));
        spots.add(new CarParkingSpot(2, "Car"));
        spots.add(new BikeParkingSpot(3, "Bike"));
        spots.add(new BikeParkingSpot(4, "Bike"));

        ParkingLot parkingLot = new ParkingLot(spots);

        ParkingFeeStrategy basic = new BasicHourlyRateStrategy();
        ParkingFeeStrategy premium = new PremiumRateStrategy();

        Vehicle car = VehicleFactory.createVehicle("Car", "CAR123", basic);
        Vehicle bike = VehicleFactory.createVehicle("Bike", "BIKE456", premium);

        ParkingSpot carSpot = parkingLot.parkVehicle(car);
        ParkingSpot bikeSpot = parkingLot.parkVehicle(bike);

        Scanner sc = new Scanner(System.in);
        System.out.println("1. Credit Card");
        System.out.println("2. Cash");
        int choice = sc.nextInt();

        double carFee = car.calculateFee(2, DurationType.HOURS);
        new Payment(carFee, getPaymentStrategy(choice)).processPayment();

        double bikeFee = bike.calculateFee(3, DurationType.HOURS);
        new Payment(bikeFee, getPaymentStrategy(choice)).processPayment();

        parkingLot.vacateSpot(carSpot, car);
        parkingLot.vacateSpot(bikeSpot, bike);

        sc.close();
    }
}

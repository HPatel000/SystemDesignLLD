package Practice.QuickRevision;

import java.util.*;

public class ParkingLotSystem {
    public static void main(String[] args) throws InterruptedException {
        Map<VEHICLE_TYPE, PricingStrategy> pricingMap = new HashMap<>();
        pricingMap.put(VEHICLE_TYPE.TWO_WHEELER, new TwoWheeler());
        pricingMap.put(VEHICLE_TYPE.FOUR_WHEELER, new FourWheeler());
        pricingMap.put(VEHICLE_TYPE.TRUCK, (durationMillis) -> 40);
        ParkingLot parkingLot = new ParkingLot("XYZ", pricingMap);
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addParkingSpot(new ParkingSpot(VEHICLE_TYPE.TWO_WHEELER));
        floor1.addParkingSpot(new ParkingSpot(VEHICLE_TYPE.TWO_WHEELER));
        floor1.addParkingSpot(new ParkingSpot(VEHICLE_TYPE.FOUR_WHEELER));
        floor1.addParkingSpot(new ParkingSpot(VEHICLE_TYPE.FOUR_WHEELER));
        floor1.addParkingSpot(new ParkingSpot(VEHICLE_TYPE.TRUCK));
        parkingLot.addFloor(floor1);

        Ticket t1 = parkingLot.parkVehicle(new Bike("bike1"));
        Thread.sleep(1000);
        if(t1 != null) {
            Receipt r1 = parkingLot.unParkVehicle(t1);
            System.out.println(r1.amount());
        }
    }
}

class ParkingLot {
    private String name;
    List<ParkingFloor> parkingFloorList;
    Map<VEHICLE_TYPE, PricingStrategy> pricingMap;

    ParkingLot(String _name, Map<VEHICLE_TYPE, PricingStrategy> _pricingMap) {
        name = _name;
        pricingMap = _pricingMap;
        parkingFloorList = new ArrayList<>();
    }

    void addFloor(ParkingFloor _parkingFloor) {
        parkingFloorList.add(_parkingFloor);
    }

    Ticket parkVehicle(Vehicle vehicle) {
        for(ParkingFloor floor: parkingFloorList) {
            ParkingSpot spot = floor.getParkingSpot(vehicle.vehicleType);
            if(spot != null) {
                return new Ticket(UUID.randomUUID(), vehicle, System.currentTimeMillis(), spot);
            }
        }
        System.out.println("No spot available");
        return null;
    }

    Receipt unParkVehicle(Ticket ticket) {
        long outTime = System.currentTimeMillis();
        long duration = outTime - ticket.parkTime();

        PricingStrategy strategy = pricingMap.get(ticket.vehicle().vehicleType);
        double amount = strategy.calculate(duration);

        ticket.spot().unParkVehicle();

        return new Receipt(UUID.randomUUID(), outTime, amount);
    }

}

class ParkingFloor {
    private final int floorId;
    List<ParkingSpot> parkingSpotList;

    ParkingFloor(int _floorId) {
        this.floorId = _floorId;
        parkingSpotList = new ArrayList<>();
    }

    void addParkingSpot(ParkingSpot _parkingSpot) {
        parkingSpotList.add(_parkingSpot);
    }

    ParkingSpot getParkingSpot(VEHICLE_TYPE vehicleType) {
        for(ParkingSpot spot: parkingSpotList) {
            if(spot.parkVehicle(vehicleType)) return spot;
        }
        return null;
    }
}

class ParkingSpot {
    private UUID spotId;
    private final VEHICLE_TYPE spotType;
    private boolean isOccupied;

    ParkingSpot(VEHICLE_TYPE _spotType) {
        spotType = _spotType;
        spotId = UUID.randomUUID();
        isOccupied = false;
    }

    boolean parkVehicle(VEHICLE_TYPE vehicleType) {
        if(vehicleType == spotType && !isOccupied) {
            isOccupied = true;
            return true;
        }
        return false;
    }

    void unParkVehicle() {
        isOccupied = false;
    }
}

interface PricingStrategy {
    double calculate(long durationMillis);
}

class TwoWheeler implements PricingStrategy {
    public double calculate(long duration) { return ((double) duration / 3600) * 100; }
}

class FourWheeler implements PricingStrategy {
    public double calculate(long duration) { return ((double) duration / 3600) * 200; }
}

record Ticket(UUID id, Vehicle vehicle, long parkTime, ParkingSpot spot) {}

record Receipt (UUID receiptId, long outTime, double amount) {}

abstract class Vehicle {
    String licensePlate;
    VEHICLE_TYPE vehicleType;

    Vehicle(String _licensePlate, VEHICLE_TYPE _vehicleType) {
        licensePlate = _licensePlate;
        vehicleType = _vehicleType;
    }
}

class Car extends Vehicle {
    Car(String licensePlate) {
        super(licensePlate, VEHICLE_TYPE.FOUR_WHEELER);
    }
}

class Bike extends Vehicle {
    Bike(String licensePlate) {
        super(licensePlate, VEHICLE_TYPE.TWO_WHEELER);
    }
}

class Truck extends Vehicle {
    Truck(String licensePlate) {
        super(licensePlate, VEHICLE_TYPE.TRUCK);
    }
}

enum VEHICLE_TYPE {
    FOUR_WHEELER, TWO_WHEELER, TRUCK
}

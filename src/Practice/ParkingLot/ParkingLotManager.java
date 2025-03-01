package Practice.ParkingLot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingLotManager {
    public static void main(String[] args) throws Exception {
        ParkingLot parkingLot = ParkingLot.getInstance();
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        parkingSpotList.add(new ParkingSpot(1, VehicleType.TWO_WHEELER));
        parkingSpotList.add(new ParkingSpot(2, VehicleType.THREE_WHEELER));
        parkingSpotList.add(new ParkingSpot(3, VehicleType.FOUR_WHEELER));
        parkingLot.addFloor(new Floor(1, parkingSpotList));
        Vehicle bike = new Vehicle("123", VehicleType.TWO_WHEELER);
        EntryGate entryGate1 = new EntryGate(bike);
        Ticket ticket = entryGate1.getTicketIfParkingAvailable();
        if(ticket != null) {
            ExitGate exitGate1 = new ExitGate(ticket, new DefaultBillingStrategy());
            System.out.println(exitGate1.getBill());
        }
    }
}

class ParkingLot {
    private static ParkingLot instance;
    private static List<Floor> floorList;
    private static ParkingStrategy parkingStrategy;

    private ParkingLot(List<Floor> floorListParam, ParkingStrategy parkingStrategyParam) {
        floorList = floorListParam;
        parkingStrategy = parkingStrategyParam;
    }

    public static ParkingLot getInstance() {
        if(instance == null) return instance = new ParkingLot(new ArrayList<>(), new DefaultParkingStrategy());
        return instance;
    }

    public void addFloor(Floor floor) {
        floorList.add(floor);
    }

    public void removeFloor(int id) {
        for(int i=0; i<floorList.size(); i++) {
            if(floorList.get(i).getId() == id) {
                floorList.remove(i);
                break;
            }
        }
    }

    public static void setParkingStrategy(ParkingStrategy parkingStrategyParam) {
        parkingStrategy = parkingStrategyParam;
    }

    public static ParkingSpot findParkingSpot(VehicleType vehicleType) {
        return parkingStrategy.getParkingSpot(vehicleType, floorList);
    }

    public static ParkingSpot findParkingSpot(VehicleType vehicleType, ParkingStrategy parkingStrategy) {
        return parkingStrategy.getParkingSpot(vehicleType, floorList);
    }
}


class Floor {
    private final int id;
    private final List<ParkingSpot> parkingSpotList;
    private final boolean isSpotAvailable;

    public Floor(int id, List<ParkingSpot> parkingSpotList) {
        this.id = id;
        this.parkingSpotList = parkingSpotList;
        isSpotAvailable = true;
    }

    public void addParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpotList.add(parkingSpot);
    }

    public void removeParkingSpot(int id) {
        for(ParkingSpot parkingSpot: this.parkingSpotList) {
            if(parkingSpot.getId() == id) {
                parkingSpotList.remove(parkingSpot);
                break;
            }
        }
    }

    public int getId() {
        return id;
    }

    public List<ParkingSpot> getParkingSpotList() {
        return parkingSpotList;
    }

    public boolean isSpotAvailable() {
        return isSpotAvailable;
    }
}

class ParkingSpot {
    private final int id;
    private final VehicleType spotType;
    private boolean isAvailable;

    public ParkingSpot(int id, VehicleType spotType) {
        this.id = id;
        this.spotType = spotType;
        this.isAvailable = true;
    }

    public VehicleType getSpotType() {
        return spotType;
    }

    public int getId() {
        return id;
    }

    public void parkVehicle(VehicleType vehicleType) throws Exception {
        if(vehicleType != this.spotType) {
            throw new Exception("This Vehicle can not be parked here.");
        }
        this.isAvailable = false;
    }

    public void unParkVehicle() {
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }
}

class EntryGate {
    private final Vehicle vehicle;

    public EntryGate(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Ticket getTicketIfParkingAvailable() throws Exception {
        ParkingSpot parkingSpot = ParkingLot.findParkingSpot(this.vehicle.getVehicleType());
        if(parkingSpot == null) {
            System.out.println("Sorry, Parking Lot is Full.");
            return null;
        }
        parkingSpot.parkVehicle(this.vehicle.getVehicleType());
        return new Ticket(1, vehicle, parkingSpot);
    }
}

class ExitGate {
    private final Ticket ticket;
    private final BillingStrategy billingStrategy;

    public ExitGate(Ticket ticket, BillingStrategy billingStrategy) {
        this.ticket = ticket;
        this.billingStrategy = billingStrategy;
    }

    public int getBill() {
        ticket.getParkingSpot().unParkVehicle();
        return billingStrategy.getBill(ticket);
    }
}

class Vehicle {
    private final String license;
    private final VehicleType vehicleType;

    public Vehicle(String license, VehicleType vehicleType) {
        this.license = license;
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getLicense() {
        return license;
    }
}

class Ticket {
    private final int id;
    private final ParkingSpot parkingSpot;
    private final Date startDate;
    private final Vehicle vehicle;

    public Ticket(int id, Vehicle vehicle, ParkingSpot parkingSpot) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.startDate = new Date();
    }

    public int getId() {
        return id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}

enum VehicleType {
    TWO_WHEELER,
    THREE_WHEELER,
    FOUR_WHEELER;

    public static int getValue(VehicleType vehicleType) {
        if(vehicleType == TWO_WHEELER) return 100;
        if(vehicleType == THREE_WHEELER) return 150;
        if(vehicleType == FOUR_WHEELER) return 200;
        return 500;
    }
}

interface ParkingStrategy {
    ParkingSpot getParkingSpot(VehicleType vehicleType, List<Floor> floorList);
}

class DefaultParkingStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot getParkingSpot(VehicleType vehicleType, List<Floor> floorList) {
        for(Floor floor: floorList) {
            for(ParkingSpot parkingSpot: floor.getParkingSpotList()) {
                if(parkingSpot.isAvailable() && parkingSpot.getSpotType() == vehicleType) return parkingSpot;
            }
        }
        return null;
    }
}

interface BillingStrategy {
    int getBill(Ticket ticket);
}

class DefaultBillingStrategy implements BillingStrategy {
    @Override
    public int getBill(Ticket ticket) {
        long diff = new Date().getTime() - ticket.getStartDate().getTime();
        long diffMinutes = diff / (60 * 60 * 1000);
        return (int)diffMinutes * VehicleType.getValue(ticket.getVehicle().getVehicleType());
    }
}
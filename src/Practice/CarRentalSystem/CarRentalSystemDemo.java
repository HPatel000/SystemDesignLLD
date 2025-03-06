package Practice.CarRentalSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CarRentalSystemDemo {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1l, "John", "xyz"));
        List<Store> stores = new ArrayList<>(
                Arrays.asList(
                        new Store(new Location("India", "Banglore", "New York", "123412"),
                        new VehicleInventory())
                )
        );
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Car("12343", "qer", "qwrfef"));
        VehicleRentalSystem vehicleRentalSystem = new VehicleRentalSystem(stores, users);
        Store store = vehicleRentalSystem.getStore(new Location("India", "Banglore", "New York", "123412"));
        Reservation reservation = new Reservation(1l, vehicles.get(0), users.get(0));

        Bill bill = new Bill(reservation);
        System.out.println(bill.calculateBill());
        Payment payment = new Payment(1l, bill.calculateBill(), PaymentMode.CASH);

        payment.payBill();

        store.completeReservation(reservation.getReservationId());

    }
}

class VehicleRentalSystem {
    List<Store> storeList;
    List<User> userList;

    public VehicleRentalSystem(List<Store> storeList, List<User> userList) {
        this.storeList = storeList;
        this.userList = userList;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Store getStore(Location location) {
//        get nearest stores based on given location
        if(storeList.isEmpty()) return null;
        return storeList.getFirst();
    }
}

class Store {
    Location location;
    List<Reservation> reservationList;
    VehicleInventory vehicleInventory;

    public Store(Location location, VehicleInventory vehicleInventory) {
        this.location = location;
        this.reservationList = new ArrayList<>();
        this.vehicleInventory = vehicleInventory;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public VehicleInventory getVehicleInventory() {
        return vehicleInventory;
    }

    public void setVehicleInventory(VehicleInventory vehicleInventory) {
        this.vehicleInventory = vehicleInventory;
    }

    public void makeReservation(Vehicle vehicle, User user) {
        Reservation reservation = new Reservation(1l, vehicle, user);
        reservationList.add(reservation);
    }

    public boolean completeReservation(Long reservationId) {
        return true;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        vehicleInventory.setVehicleList(vehicles);
    }

    public List<Vehicle> getVehicles() {
        return vehicleInventory.getVehicleList();
    }
}

class VehicleInventory {
    List<Vehicle> vehicleList;

    public List<Vehicle> getVehicleList() {
//        filter vehicle based on given para
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}

class Location {
    private final String country;
    private final String state;
    private final String city;
    private final String pincode;

    public Location(String country, String state, String city, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
    }
}

class User {
    private final Long userId;
    private final String name;
    private final String license;

    public User(Long userId, String name, String license) {
        this.userId = userId;
        this.name = name;
        this.license = license;
    }
}

class Reservation {
    private final Long reservationId;
    private final Vehicle vehicle;
    private final User user;
    private final Date bookingDate;
    private Date fromDate;
    private Date toDate;
    private Location pickupLocation;
    private Location dropLocation;
    private final ReservationStatus reservationStatus;

    public Reservation(Long reservationId, Vehicle vehicle, User user) {
        this.reservationId = reservationId;
        this.vehicle = vehicle;
        this.user = user;
        this.bookingDate = new Date();
        this.reservationStatus = ReservationStatus.SCHEDULED;
    }

    public Long getReservationId(){
        return reservationId;
    }
}

class Payment {
    private final Long paymentId;
    private final Double amountPaid;
    private final PaymentMode paymentMode;

    public Payment(Long paymentId, Double amountPaid, PaymentMode paymentMode) {
        this.paymentId = paymentId;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
    }

    public void payBill() {
//        paybill using selected payment mode
    }
}

class Bill {
    private final Reservation reservation;
    private double amount;
    private boolean isPaid;

    public Bill(Reservation reservation) {
        this.reservation = reservation;
    }

    public double calculateBill() {
//        count bill
        return 1000;
    }
}

abstract class Vehicle {
    private final String registrationNo;
    private final String model;
    private final String make;

    public Vehicle(String registrationNo, String model, String make) {
        this.registrationNo = registrationNo;
        this.model = model;
        this.make = make;
    }
}

class Car extends Vehicle {

    public Car(String registrationNo, String model, String make) {
        super(registrationNo, model, make);
    }
}

class Bike extends Vehicle {

    public Bike(String registrationNo, String model, String make) {
        super(registrationNo, model, make);
    }
}

enum PaymentMode {
    CASH, ONLINE;
}

enum ReservationStatus {
    SCHEDULED, INPROGRESS, COMPLETED, CANCELLED
}



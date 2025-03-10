package DesignPatterns;

public class NullObjectPattern {
    public static void main(String[] args) {
//        no need for null check default null object will be returned
//        which will prevent null pointer exception
        Vehicle vehicle = VehicleFactory.getVehicle("bike");
        vehicle.start();
    }
}

interface Vehicle {
    void start();
}

class CarObj implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car is started");
    }
}

class NullObj implements Vehicle {
    @Override
    public void start() {
        System.out.println("Vehicle is started");
    }
}

class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        if(type.equals("car")) return new CarObj();
        return new NullObj();
    }
}

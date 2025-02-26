package DesignPatterns;

/**
 * Strategy
 * Behavioral
 * Defines a family of algorithms and allows them to be interchangeable within a class.
 * When you need to choose between different behaviors or algorithms at runtime
 * (e.g., sorting strategies, payment processing).
 */
public class StrategyPattern {
    public static void main(String[] args) {
        Vehical car = new Car();
        car.drive();
        Vehical sportsCar = new SportsCar();
        sportsCar.drive();
    }
}


class Vehical {
    private final DriveStrategy driveStrategy;

    public Vehical(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
    }

    public void drive() {
        driveStrategy.drive();
    }
}

class Car extends Vehical {

    public Car() {
        super(new NormalDriveStrategy());
    }
}

class SportsCar extends Vehical {
    public SportsCar() {
        super(new SpecialDriveStrategy());
    }
}

interface DriveStrategy {
    void drive();
}

class NormalDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Normal Driving");
    }
}
class SpecialDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Special Driving");
    }
}
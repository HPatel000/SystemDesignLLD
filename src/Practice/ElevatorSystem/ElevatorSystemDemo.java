package Practice.ElevatorSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
//https://github.com/swatijha-2906/LowLevelDesigns/tree/main/3_ElevatorSystem/ElevatorSystem/src/main/java
public class ElevatorSystemDemo {
    public static void main(String[] args) {
        ElevatorSystem elevatorSystem= ElevatorSystem.INSTANCE;
        elevatorSystem.setElevatorSelectionStrategy(new OddEvenStrategy());
        elevatorSystem.setElevatorControlStrategy(new LookAlgorithm());

        //add Floors
        int totalFloors= 50;
        for(int i=0; i<=totalFloors; i++)
        {
            elevatorSystem.addFloor(new Floor(i));
        }
        System.out.println("No. of floors added " + totalFloors);

        //add elevators
        int totalElevators = 4;
        for(int i=1; i<=totalElevators; i++)
        {
            elevatorSystem.addElevator(new ElevatorController(i));
        }
        System.out.println("No. of elevators added " + totalElevators);

        //Working

        //Request 1
        System.out.println("Person at floor 1 presses UP Button");
        for(Floor floor: ElevatorSystem.INSTANCE.floors)
        {
            if(floor.getId()==1)
                floor.pressButton(Direction.UP);
        }

        //Request 2
        System.out.println("Person at floor 5 presses UP Button");
        for(Floor floor: ElevatorSystem.INSTANCE.floors)
        {
            if(floor.getId()==5)
                floor.pressButton(Direction.UP);
        }

        //Request 3
        System.out.println("Person presses 10 in elevator 2");
        for(ElevatorController elevatorController: ElevatorSystem.INSTANCE.getElevatorControllerList())
        {
            if(elevatorController.getId()==2)
                elevatorController.getElevatorCar().pressButton(10);

        }

        //Request 4
        System.out.println("Person presses 6 in elevator 2");
        for(ElevatorController elevatorController: ElevatorSystem.INSTANCE.getElevatorControllerList())
        {
            if(elevatorController.getId()==2)
                elevatorController.getElevatorCar().pressButton(6);
        }

        //Request 5
        System.out.println("Person at floor 7 presses DOWN Button");
        for(Floor floor: ElevatorSystem.INSTANCE.floors)
        {
            if(floor.getId()==7)
                floor.pressButton(Direction.DOWN);
        }

        //Request 6
        System.out.println("Person presses 1 in elevator 3");
        for(ElevatorController elevatorController: ElevatorSystem.INSTANCE.getElevatorControllerList())
        {
            if(elevatorController.getId()==3)
                elevatorController.getElevatorCar().pressButton(1);
        }

    }
}

class ElevatorSystem {
    private List<ElevatorController> elevatorControllerList = new ArrayList<ElevatorController>();
    public static ElevatorControlStrategy elevatorControlStrategy;
    public static ElevatorSelectionStrategy elevatorSelectionStrategy;
    public List<Floor> floors = new ArrayList<Floor>();

    public static ElevatorSystem INSTANCE = new ElevatorSystem();

    public List<ElevatorController> getElevatorControllerList() {
        return elevatorControllerList;
    }

    public void setElevatorControllerList(List<ElevatorController> elevatorControllerList) {
        this.elevatorControllerList = elevatorControllerList;
    }

    public static ElevatorControlStrategy getElevatorControlStrategy() {
        return elevatorControlStrategy;
    }

    public static ElevatorSelectionStrategy getElevatorSelectionStrategy() {
        return elevatorSelectionStrategy;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public static ElevatorSystem getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(ElevatorSystem INSTANCE) {
        ElevatorSystem.INSTANCE = INSTANCE;
    }

    private ElevatorSystem() {

    }

    public void addElevator(ElevatorController e) {
        elevatorControllerList.add(e);
    }

    public void removeElevator(ElevatorController e) {
        elevatorControllerList.remove(e);
    }

    public void setElevatorControlStrategy(ElevatorControlStrategy elevatorControlStrategy) {
        ElevatorSystem.elevatorControlStrategy = elevatorControlStrategy;
    }

    public void setElevatorSelectionStrategy(ElevatorSelectionStrategy elevatorSelectionStrategy) {
        ElevatorSystem.elevatorSelectionStrategy = elevatorSelectionStrategy;
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

}


class ElevatorCar {
    private int id;
    private Door door;
    private Display display;
    private Button button;
    private int currentFloor;  //updated while elevator moves to each floor
    private Direction dir; //updated every time elevator hanges direction


    public ElevatorCar(int id) {
        this.id = id;
        door = new Door();
        display = new Display();
        currentFloor = 0;
        dir = Direction.NONE;
        button = new InternalButton();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void move(Direction dir, int floor) {
        System.out.println("Elevator " + id + "moving " + dir);
        System.out.println("Elevator " + id + "stops at floor " + floor);
        door.open(id);
        door.close(id);

        //called everytime when currFloor value changes
        setDisplay();

    }

    public void pressButton(int floor) {
        Direction dir = Direction.NONE;
        if (floor > currentFloor)
            dir = Direction.UP;
        else if (floor < currentFloor)
            dir = Direction.DOWN;
        button.pressButton(floor, dir, id);
    }


    private void setDisplay() {
        display.setFloor(currentFloor);
        display.setDirection(dir);

    }


}

class ElevatorController {

    private int id;
    private ElevatorCar elevatorCar;

    public ElevatorController(int id) {
        this.id = id;
        elevatorCar = new ElevatorCar(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ElevatorCar getElevatorCar() {
        return elevatorCar;
    }

    public void setElevatorCar(ElevatorCar elevatorCar) {
        this.elevatorCar = elevatorCar;
    }

    public void acceptRequest(int floor, Direction dir) {
        ElevatorSystem.elevatorControlStrategy.getPendingRequestList().add(new PendingRequests(floor, dir));

        controlCar();
    }

    private void controlCar() {

        ElevatorSystem.elevatorControlStrategy.moveElevator(this);
        System.out.println("Elevator moving...");
    }

}


class PendingRequests {
    private int floor;
    private Direction dir;

    public PendingRequests(int floor, Direction dir) {
        this.floor = floor;
        this.dir = dir;
    }

}


class ElevatorControlStrategy {
    //queue storing pending requests in form of
    private Queue<PendingRequests> pendingRequestList = new LinkedList<PendingRequests>();
    private List<ElevatorController> elevatorControllerList = ElevatorSystem.INSTANCE.getElevatorControllerList();

    public Queue<PendingRequests> getPendingRequestList() {
        return pendingRequestList;
    }

    public void setPendingRequestList(Queue<PendingRequests> pendingRequestList) {
        this.pendingRequestList = pendingRequestList;
    }

    public List<ElevatorController> getElevatorControllerList() {
        return elevatorControllerList;
    }

    public void setElevatorControllerList(List<ElevatorController> elevatorControllerList) {
        this.elevatorControllerList = elevatorControllerList;
    }

    public void moveElevator(ElevatorController elevatorController) {

    }
}


class ElevatorSelectionStrategy {
    protected List<ElevatorController> elevatorControllerList = ElevatorSystem.INSTANCE.getElevatorControllerList();

    public int selectElevator(int floor, Direction dir) {
        return 0;
    }
}

class Floor {
    private int id;
    private Display display;
    private Button button;

    public Floor(int id) {
        this.id = id;
        button = new ExternalButton();
    }

    public void pressButton(Direction dir) {
        button.pressButton(id, dir);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    //called everytime selected elevator moves each floor
    private void setDisplay(int floor, Direction dir) {
        display.setDirection(dir);
        display.setFloor(floor);
    }

}


class Display {
    private int floor;
    private Direction direction;

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    private Display display;
}

class Button {
    //for External Button
    public void pressButton(int floor, Direction dir) {
    }

    //for Internal Button
    public void pressButton(int floor, Direction dir, int elevatorId) {
    }
}


class Door {
    public void open(int id) {
        System.out.println("Door open for " + id);
    }

    public void close(int id) {
        System.out.println("Door close for " + id);
    }
}

class InternalButton extends Button {
    private InternalDispatcher idispatcher;
    private List<Integer> floors = new ArrayList<Integer>();

    public InternalButton() {
        idispatcher = new InternalDispatcher();
    }

    public void pressButton(int floor, Direction dir, int elevatorId) {

        floors.add(floor);
        System.out.println("Pressed floor " + floor + " from elevator " + elevatorId);
        idispatcher.submitRequest(floor, dir, elevatorId);
    }
}


class ExternalButton extends Button {
    private final ExternalDispatcher edispatcher = ExternalDispatcher.INSTANCE;
    private Direction direction;

    public void pressButton(int floor, Direction dir, Long id) {
        direction = dir;
        System.out.println("Pressed " + direction + " from floor " + floor);
        edispatcher.submitRequest(floor, dir);
    }
}

class InternalDispatcher {

    public void submitRequest(int floor, Direction dir, int elevatorId) {
        for (ElevatorController eController : ElevatorSystem.INSTANCE.getElevatorControllerList()) {
            if (eController.getId() == elevatorId) {
                eController.acceptRequest(floor, dir);
            }
        }
    }
}


class ExternalDispatcher {

    public static ExternalDispatcher INSTANCE = new ExternalDispatcher();

    private ExternalDispatcher() {

    }

    public void submitRequest(int floor, Direction dir) {
        int elevatorId = ElevatorSystem.elevatorSelectionStrategy.selectElevator(floor, dir);
        System.out.println("Selected elevator " + elevatorId);
        for (ElevatorController eController : ElevatorSystem.elevatorControlStrategy.getElevatorControllerList()) {
            if (eController.getId() == elevatorId) {
                eController.acceptRequest(floor, dir);
            }
        }
    }
}

class FirstComeFirstServe extends ElevatorControlStrategy {
    public void moveElevator(ElevatorController elevatorController) {
        //poll each requests out of queue one by one
        //move elevator according to each request
        //Disadvantage: frequent change of direction of elevator, hence inefficient and
        // long waiting time for users

    }
}

class LookAlgorithm extends ElevatorControlStrategy {
    public void moveElevator(ElevatorController elevatorController) {
//        In this algorithm, the elevator moves in a specific direction,
//        but instead of going all the way to the end of the building before reversing direction
//        like the SCAN algorithm, it reverses direction as soon as it reaches the last
//        request in the current direction.

//        Implemented using a min heap, a max heap and a queue
//        Min heap: all requests that can be served in UP direction
//        (eg. Requested floor> currFloor, requested UP, elevator moving UP)
//        Max heap: all requests that can be served in DOWN direction (floor < currFloor)
//        (eg. Requested floor< currFloor, requested DOWN, elevator moving DOWN)
//        Queue: all requests that cannot be served in current direction
//        eg. elevator moving up and currFloor is 3, now someone at floor 1 requests up

//        While moving up, all requests from min heap will be taken one by one
//        when min heap is empty, elevator reverses direction and all UP requests from queue
//        will be put in min heap.

//        While moving down, all requests from max heap will be taken one by one
//        when max heap is empty, elevator reverses direction and all DOWN requests from queue
//        will be put in max heap.

//        Advantage:
//        1. not frequent change of floor for every request
//        2. no starvation of requests
//        3. efficient in terms of usage because it moves only the areas of the requested floors.


//        Disadvantage: it does not prioritize requests based on their urgency or importance.

    }
}


class OddEvenStrategy extends ElevatorSelectionStrategy {


    @Override
    public int selectElevator(int floor, Direction dir) {
        for (ElevatorController eController : elevatorControllerList) {
            //old elevator for odd floors and even elevators for even floors
            //select elevator which is moving in same direction which is requested or IDLE elevator
            if(floor%2 == eController.getId()%2)
            {
                int currFloor= eController.getElevatorCar().getCurrentFloor();
                Direction currDir= eController.getElevatorCar().getDir();
                if(floor>currFloor && currDir==Direction.UP)
                    return eController.getId();
                else if(floor<currFloor && currDir==Direction.DOWN)
                    return eController.getId();
                else if(currDir==Direction.NONE)
                    return eController.getId();

            }
        }
        return ThreadLocalRandom.current().nextInt(1, elevatorControllerList.size());
    }
}

class ZoneStrategy extends ElevatorSelectionStrategy {
    @Override
    public int selectElevator(int floor, Direction dir) {
        for (ElevatorController eController : elevatorControllerList) {
            //assign elevators according to zones in building
            //out of these elevators select the elevator which is going in the same direction or is idle
        }
        return ThreadLocalRandom.current().nextInt(1, elevatorControllerList.size());
    }
}


enum Direction {
    UP, DOWN, NONE;
}
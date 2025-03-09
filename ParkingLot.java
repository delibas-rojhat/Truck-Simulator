import java.util.ArrayList;
public class ParkingLot {
    private final int capacityConstraint;
    private final int truckLimit;
    private MyQueue waitingSection;
    private MyQueue readySection;

    public ParkingLot(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        this.waitingSection = new MyQueue();
        this.readySection = new MyQueue();

    }

    public void addTruck(Truck truck){
            waitingSection.enqueue(truck);
    }
    public Truck moveToReady() {
        if (!waitingSection.isEmpty()) {
            Truck truck = waitingSection.dequeue();
            readySection.enqueue(truck);
            return truck;
        } else {
            return null;
        }
    }

    public Truck getReadyTruck() {
        if (!readySection.isEmpty()){
            return readySection.dequeue();
        }
        else {
            return null;
        }
    }

    public boolean canAddTruck() {
        return (waitingSection.size() + readySection.size()) < truckLimit;
    }

    public int numberOfTruck() {
        return (waitingSection.size() + readySection.size());
    }
    public boolean isReadySectionEmpty() {
        return readySection.isEmpty();
    }

    public int getCapacity(){
        return capacityConstraint;
    }

    public int getTruckLimit(){
        return truckLimit;
    }

    public MyQueue getWaitingSection(){
        return waitingSection;
    }

    public MyQueue getReadySection(){
        return readySection;
    }

}

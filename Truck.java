public class Truck {
    private final int id;
    private final int maxCapacity;
    private int currentLoad;

    public Truck(int id, int maxCapacity) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.currentLoad = 0;

    }
    public int getId(){
        return id;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public int getCurrentLoad(){
        return currentLoad;
    }
    public void load(int amount) {
        if (currentLoad + amount <= maxCapacity) {
            currentLoad += amount;
        } else {
            currentLoad = maxCapacity;
        }
    }
    public void unload(){
        currentLoad = 0;
    }
    public int getRemainingCapacity(){
        return maxCapacity - currentLoad;
    }
    public Boolean isFull(){

        return currentLoad == maxCapacity;
    }

}
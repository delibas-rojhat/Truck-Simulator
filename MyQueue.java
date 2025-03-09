import java.util.ArrayList;

public class MyQueue {
    private ArrayList<Truck> my_list;

    public MyQueue(){
        my_list = new ArrayList<>();
    }
    public void enqueue(Truck truck){
        my_list.add(truck);
    }

    public Truck dequeue(){
        if (isEmpty()){
            return null;
        }
        return my_list.remove(0);
    }

    public boolean isEmpty(){
        return my_list.isEmpty();
    }

    public int size(){
        return my_list.size();
    }


}

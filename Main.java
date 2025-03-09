import java.io.*;
public class Main {
    private static AVLTree parkingLots = new AVLTree();
    private static AVLTree nonEmptyLots = new AVLTree();


    public static void main(String[] args) throws IOException {
        //if (args.length < 2) {
          //  return;
        //}
        long startTime = System.currentTimeMillis();

        String inputFile = "type2-large.txt";
        String outputFile = "output.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String command = parts[0];

                switch (command) {
                    case "create_parking_lot":
                        int capacityConstraint = Integer.parseInt(parts[1]);
                        int truckLimit = Integer.parseInt(parts[2]);
                        createParkingLot(capacityConstraint, truckLimit);
                        break;

                    case "delete_parking_lot":
                        int capacityToDelete = Integer.parseInt(parts[1]);
                        deleteParkingLot(capacityToDelete);
                        break;

                    case "add_truck":
                        int truckId = Integer.parseInt(parts[1]);
                        int truckCapacity = Integer.parseInt(parts[2]);
                        addTruck(truckId, truckCapacity, writer);
                        break;

                    case "ready":
                        int readyCapacity = Integer.parseInt(parts[1]);
                        makeTruckReady(readyCapacity, writer);
                        break;

                    case "load":
                        int loadCapacity = Integer.parseInt(parts[1]);
                        int loadAmount = Integer.parseInt(parts[2]);
                        load(loadCapacity, loadAmount, writer);
                        break;

                    case "count":
                        int countCapacity = Integer.parseInt(parts[1]);
                        count(countCapacity, writer);
                        break;

                }
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Çalışma süresi: " + duration + " ms");
    }
    private static void createParkingLot(int capasityConstraint, int truckLimit) {
        ParkingLot parkingLot = new ParkingLot(capasityConstraint, truckLimit);
        parkingLots.insert(parkingLot);
        nonEmptyLots.insert(parkingLot);
    }

    private static void deleteParkingLot(int capacityConstraint) {
        parkingLots.deleteParkingLot(capacityConstraint);
        nonEmptyLots.deleteParkingLot(capacityConstraint);
    }

    private static void addTruck(int truckId, int capacity, BufferedWriter writer) throws IOException {
        Truck truck = new Truck(truckId, capacity);
        ParkingLot parkingLot = parkingLots.search(capacity);

        if (parkingLot != null && parkingLot.canAddTruck()) {
            parkingLot.addTruck(truck);
            writer.write(parkingLot.getCapacity() + "\n");
            return;
        } parkingLot = parkingLots.findSmaller(capacity);
        while (parkingLot != null) { //searches suitable parking lot for truck
            if (parkingLot.canAddTruck()) {
                parkingLot.addTruck(truck);
                writer.write(parkingLot.getCapacity() + "\n");
                return;
            }
            parkingLot = parkingLots.findSmaller(parkingLot.getCapacity()); // tries to find largest smalles if truck is not added
        }

        writer.write("-1\n");
    }

    private static void makeTruckReady(int capacity, BufferedWriter writer) throws IOException {
        ParkingLot parkingLot = parkingLots.search(capacity);
        Truck truck;

        if (parkingLot != null) {
            truck = parkingLot.moveToReady();
            if (truck != null) {
                writer.write(truck.getId() + " " + parkingLot.getCapacity() + "\n");
                return;
            }
        }

        parkingLot = parkingLots.findBigger(capacity);
        while (parkingLot != null){
            truck = parkingLot.moveToReady();
            if (truck != null) { // truck moves to ready if it exists
                writer.write(truck.getId() + " " + parkingLot.getCapacity() + "\n");
                return;
            } else {
                parkingLot = parkingLots.findBigger(parkingLot.getCapacity()); // finds parking lot which has bigger capacity
            }
        }
        writer.write("-1\n");

    }

    private static void load(int capacity, int loadAmount, BufferedWriter writer) throws IOException {
        int remaningLoad = loadAmount;
        int loadedAmount;
        Truck truck;
        ParkingLot parkingLot = parkingLots.search(capacity);
        String outputLine = "";

        if (parkingLot != null) { // checks the case which
            while (!parkingLot.isReadySectionEmpty() && remaningLoad > 0) {
                truck = parkingLot.getReadyTruck();
                loadedAmount = Math.min(parkingLot.getCapacity(), remaningLoad);
                truck.load(loadedAmount);
                remaningLoad -= loadedAmount;

                if (truck.isFull()) {
                    truck.unload();
                }

                int new_lot_capacity = truck.getRemainingCapacity();

                ParkingLot new_ParkingLot_ofTruck = parkingLots.search(new_lot_capacity);

                if (new_ParkingLot_ofTruck != null && new_ParkingLot_ofTruck.canAddTruck()) {
                    new_ParkingLot_ofTruck.addTruck(truck);
                    outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                } else {
                    new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                    if (new_ParkingLot_ofTruck == null) {
                        outputLine += truck.getId() + " -1 - ";
                    } else {
                        boolean isAdded = false;
                        while (new_ParkingLot_ofTruck != null) {

                            if (new_ParkingLot_ofTruck.canAddTruck()) {
                                new_ParkingLot_ofTruck.addTruck(truck);
                                outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                                isAdded = true;
                                break;
                            } else {
                                new_lot_capacity = new_ParkingLot_ofTruck.getCapacity();
                                new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                            }
                        }
                        if (!isAdded) {
                            outputLine += truck.getId() + " -1 - ";
                        }
                    }


                }
            }
            if (remaningLoad > 0) {
                parkingLot = parkingLots.findBigger(capacity);
                while (parkingLot != null) {
                    if (!parkingLot.isReadySectionEmpty()) {
                        truck = parkingLot.getReadyTruck();
                        loadedAmount = Math.min(parkingLot.getCapacity(), remaningLoad);
                        truck.load(loadedAmount);
                        remaningLoad -= loadedAmount;

                        if (truck.isFull()) {
                            truck.unload();
                        }

                        int new_lot_capacity = truck.getRemainingCapacity();

                        ParkingLot new_ParkingLot_ofTruck = parkingLots.search(new_lot_capacity);

                        if (new_ParkingLot_ofTruck != null && new_ParkingLot_ofTruck.canAddTruck()) {
                            new_ParkingLot_ofTruck.addTruck(truck);
                            outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                        } else {
                            new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                            if (new_ParkingLot_ofTruck == null) {
                                outputLine += truck.getId() + " -1 - ";
                            } else {
                                boolean isAdded = false;
                                while (new_ParkingLot_ofTruck != null) {

                                    if (new_ParkingLot_ofTruck.canAddTruck()) {
                                        new_ParkingLot_ofTruck.addTruck(truck);
                                        outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                                        isAdded = true;
                                        break;
                                    } else {
                                        new_lot_capacity = new_ParkingLot_ofTruck.getCapacity();
                                        new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                                    }
                                }
                                if (!isAdded) {
                                    outputLine += truck.getId() + " -1 - ";
                                }
                            }

                        }
                        if (remaningLoad <= 0) break;
                    }
                    parkingLot = parkingLots.findBigger(parkingLot.getCapacity());
                }
            }


        } else {
            if (remaningLoad > 0) {
                parkingLot = parkingLots.findBigger(capacity);
                while (parkingLot != null) {
                    if (!parkingLot.isReadySectionEmpty()) {
                        truck = parkingLot.getReadyTruck();
                        loadedAmount = Math.min(parkingLot.getCapacity(), remaningLoad);
                        truck.load(loadedAmount);
                        remaningLoad -= loadedAmount;

                        if (truck.isFull()) {
                            truck.unload();
                        }

                        int new_lot_capacity = truck.getRemainingCapacity();

                        ParkingLot new_ParkingLot_ofTruck = parkingLots.search(new_lot_capacity);

                        if (new_ParkingLot_ofTruck != null && new_ParkingLot_ofTruck.canAddTruck()) {
                            new_ParkingLot_ofTruck.addTruck(truck);
                            outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                        } else {
                            new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                            if (new_ParkingLot_ofTruck == null) {
                                outputLine += truck.getId() + " -1 - ";
                            } else {
                                boolean isAdded = false;
                                while (new_ParkingLot_ofTruck != null) {

                                    if (new_ParkingLot_ofTruck.canAddTruck()) {
                                        new_ParkingLot_ofTruck.addTruck(truck);
                                        outputLine += truck.getId() + " " + new_ParkingLot_ofTruck.getCapacity() + " - ";
                                        isAdded = true;
                                        break;
                                    } else {
                                        new_lot_capacity = new_ParkingLot_ofTruck.getCapacity();
                                        new_ParkingLot_ofTruck = parkingLots.findSmaller(new_lot_capacity);
                                    }
                                }
                                if (!isAdded) {
                                    outputLine += truck.getId() + " -1 - ";
                                }
                            }

                        }
                        if (remaningLoad == 0) break;
                    }
                    if (parkingLot.isReadySectionEmpty()) {
                        parkingLot = parkingLots.findBigger(parkingLot.getCapacity());
                    }
                }
            }
        }
        if (outputLine.endsWith(" - ")) {
            outputLine = outputLine.substring(0, outputLine.length() - 3);
        }
        if (outputLine.isEmpty()) {
            outputLine = "-1";
        }

        writer.write(outputLine + "\n");
    }


    private static void count(int capacity,BufferedWriter writer) throws IOException{
        int truck_number = 0;
        ParkingLot bigger_lot = parkingLots.findBigger(capacity);
        while (bigger_lot != null) {

            truck_number += bigger_lot.numberOfTruck();
            capacity = bigger_lot.getCapacity();
            bigger_lot = parkingLots.findBigger(capacity);

        }

        writer.write(truck_number + "\n");

    }

}
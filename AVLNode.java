public class AVLNode {
    ParkingLot parkingLot;
    AVLNode left, right;
    int height;

    public AVLNode(ParkingLot parkingLot){
        this.parkingLot = parkingLot;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

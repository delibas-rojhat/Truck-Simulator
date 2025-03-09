public class AVLTree {
    AVLNode root;

    public AVLTree() {}

    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalanceIndex(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }
    }

    private AVLNode rightRotate(AVLNode y) { // y is the node which is unbalanced
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;   //  y is not root anymore
        y.left = T2;   // T2 will be new left child of y

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.right), height(x.left)) + 1;

        return x; // New root

    }

    private AVLNode leftRotate(AVLNode y) { // y is the node which is unbalanced
        AVLNode x = y.right;
        AVLNode T2 = x.left;

        x.left = y;  // y is not root anymore
        y.right = T2; // T2 will be new right child ol y

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.right), height(x.left)) + 1;

        return x; // x will be new root

    }

    public void insert(ParkingLot parkingLot) {
        root = recursiveInsert(root, parkingLot);
    }

    private AVLNode recursiveInsert(AVLNode node, ParkingLot parkingLot) {
        if (node == null) {
            return new AVLNode(parkingLot);
        }

        if (parkingLot.getCapacity() < node.parkingLot.getCapacity()) {
            node.left = recursiveInsert(node.left, parkingLot);
        } else if (parkingLot.getCapacity() > node.parkingLot.getCapacity()) {
            node.right = recursiveInsert(node.right, parkingLot);
        } else {
            return node;
        }


        node.height = 1 + Math.max(height(node.left), height(node.right));


        int balanceIndex = getBalanceIndex(node);

        if (balanceIndex > 1) { // left heavy
            if (getBalanceIndex(node.left) >= 0) { //rotate right
                return rightRotate(node);
            } else {                               // rotate left-right
                node.left = leftRotate(node.left);
                return rightRotate(node);

            }
        }

        if (balanceIndex < -1) { // right heavy
            if (getBalanceIndex(node.right) <= 0) { // rotate left
                return leftRotate(node);
            } else {                                 // rotate right-left
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node; // balanced already
    }

    public void deleteParkingLot(int capacity) {
        root = recursiveDelete(root, capacity);

    }

    private AVLNode recursiveDelete(AVLNode node, int capacity) {
        if (node == null) return null;

        if (capacity < node.parkingLot.getCapacity()) {
            node.left = recursiveDelete(node.left, capacity);
        } else if (capacity > node.parkingLot.getCapacity()) {
            node.right = recursiveDelete(node.right, capacity);
        } else {
            if (node.left == null || node.right == null) {
                if (node.left != null) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            } else {
                AVLNode temp = getMinCapacityNode(node.right);
                node.parkingLot = temp.parkingLot;
                node.right = recursiveDelete(node.right, temp.parkingLot.getCapacity());
            }
        }
        if (node == null) return null;


        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balanceIndex = getBalanceIndex(node);

        if (balanceIndex > 1) {
            if (getBalanceIndex(node.left) >= 0) {
                return rightRotate(node);
            } else {
                node.left = leftRotate(node.left);
                return rightRotate(node);

            }
        }

        if (balanceIndex < -1) {
            if (getBalanceIndex(node.right) <= 0) {
                return leftRotate(node);
            } else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }


    public ParkingLot search(int capacity) {
        return recursiveSearch(root, capacity);
    }

    private ParkingLot recursiveSearch(AVLNode node, int capacity) {
        if (node == null) {
            return null;
        }

        if (capacity < node.parkingLot.getCapacity()) {
            return recursiveSearch(node.left, capacity);
        } else if (capacity > node.parkingLot.getCapacity()) {
            return recursiveSearch(node.right, capacity);
        } else {
            return node.parkingLot;
        }
    }


    private AVLNode getMinCapacityNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;

    }

    public ParkingLot findSmaller(int capacity) { // finds largest smaller than capacity
        return findSmaller(root, capacity, null);
    }

    private ParkingLot findSmaller(AVLNode node, int capacity, ParkingLot bestMatch) {
        if (node == null) return bestMatch;

        if (node.parkingLot.getCapacity() < capacity) { // holds smaller capacity and search for larger smaller ones
            bestMatch = node.parkingLot;
            return findSmaller(node.right, capacity, bestMatch);
        } else {                                        // if it is not smaller checks left tree
            return findSmaller(node.left, capacity, bestMatch);
        }
    }

    public ParkingLot findBigger(int capacity) { // finds smallest bigger capacity
        return findBigger(root, capacity, null);
    }

    private ParkingLot findBigger(AVLNode node, int capacity, ParkingLot bestMatch) {
        if (node == null) return bestMatch;

        if (node.parkingLot.getCapacity() > capacity) { // holds bigger capacity and search for smaller bigger ones
            bestMatch = node.parkingLot;
            return findBigger(node.left, capacity, bestMatch);
        } else {                                         // if it is not bigger checks right tree
            return findBigger(node.right, capacity, bestMatch);
        }
    }



}


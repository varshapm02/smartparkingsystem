package main.java.smartparking.model;

public class ParkingSpot {
    private final String id;           // e.g., "F1-A1"
    private final int sizeLevel;       // max size level this spot can accept
    private volatile boolean occupied;
    private volatile ParkingTicket currentTicket;

    public ParkingSpot(String id, int sizeLevel) {
        this.id = id;
        this.sizeLevel = sizeLevel;
        this.occupied = false;
        this.currentTicket = null;
    }

    public String getId() { return id; }
    public int getSizeLevel() { return sizeLevel; }

    // Returns whether this spot can fit a vehicle of given size level
    public boolean canFit(int vehicleSizeLevel) {
        return !occupied && vehicleSizeLevel <= sizeLevel;
    }

    // Called when occupying; caller must hold per-spot lock
    public void occupy(ParkingTicket ticket) {
        this.occupied = true;
        this.currentTicket = ticket;
    }

    // Called when vacating; caller must hold per-spot lock
    public void vacate() {
        this.occupied = false;
        this.currentTicket = null;
    }

    public boolean isOccupied() { return occupied; }
    public ParkingTicket getCurrentTicket() { return currentTicket; }

    @Override
    public String toString() {
        return "Spot{" + id + ",size=" + sizeLevel + ",occ=" + occupied + "}";
    }
}


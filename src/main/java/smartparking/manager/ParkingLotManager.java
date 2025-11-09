package main.java.smartparking.manager;

import main.java.smartparking.model.ParkingFloor;
import main.java.smartparking.model.ParkingSpot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLotManager {
    private static final ParkingLotManager INSTANCE = new ParkingLotManager();

    // floorId -> ParkingFloor
    private final Map<String, ParkingFloor> floors = new ConcurrentHashMap<>();

    // spotId -> ReentrantLock for per-spot locking
    private final Map<String, ReentrantLock> spotLocks = new ConcurrentHashMap<>();

    private ParkingLotManager() {}

    public static ParkingLotManager getInstance() {
        return INSTANCE;
    }

    // Add a floor
    public void addFloor(ParkingFloor floor) {
        floors.put(floor.getFloorId(), floor);
        // register locks for spots
        for (String spotId : floor.getSpots().keySet()) {
            spotLocks.putIfAbsent(spotId, new ReentrantLock());
        }
    }

    // Convenience: addSpot to specific floor
    public void addSpotToFloor(String floorId, ParkingSpot spot) {
        floors.computeIfAbsent(floorId, k -> new ParkingFloor(floorId));
        ParkingFloor floor = floors.get(floorId);
        floor.addSpot(spot);
        spotLocks.putIfAbsent(spot.getId(), new ReentrantLock());
    }

    public Collection<ParkingFloor> getAllFloors() {
        return floors.values();
    }

    public ParkingFloor getFloor(String floorId) {
        return floors.get(floorId);
    }

    public ReentrantLock getLockForSpot(String spotId) {
        spotLocks.putIfAbsent(spotId, new ReentrantLock());
        return spotLocks.get(spotId);
    }

    // Find spot by id
    public ParkingSpot getSpot(String spotId) {
        for (ParkingFloor floor : floors.values()) {
            ParkingSpot s = floor.getSpots().get(spotId);
            if (s != null) return s;
        }
        return null;
    }
}


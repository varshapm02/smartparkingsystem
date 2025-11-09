package main.java.smartparking.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParkingFloor {
    private final String floorId;
    private final Map<String, ParkingSpot> spots;

    public ParkingFloor(String floorId) {
        this.floorId = floorId;
        this.spots = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public String getFloorId() { return floorId; }

    public void addSpot(ParkingSpot spot) {
        spots.put(spot.getId(), spot);
    }

    public Map<String, ParkingSpot> getSpots() { return spots; }

    @Override
    public String toString() {
        return "Floor{" + floorId + ", spots=" + spots.size() + "}";
    }
}


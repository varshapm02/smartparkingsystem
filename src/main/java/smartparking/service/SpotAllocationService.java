package main.java.smartparking.service;

import main.java.smartparking.factory.TicketFactory;
import main.java.smartparking.manager.ParkingLotManager;
import main.java.smartparking.model.ParkingFloor;
import main.java.smartparking.model.ParkingSpot;
import main.java.smartparking.model.ParkingTicket;
import main.java.smartparking.model.Vehicle;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SpotAllocationService {
    private final ParkingLotManager manager = ParkingLotManager.getInstance();

    public ParkingTicket checkIn(Vehicle vehicle) throws InterruptedException {
        int vSize = vehicle.getType().getSizeLevel();

        ParkingSpot chosen = findSpotBySizePreference(vSize, true);

        if (chosen == null) {
            chosen = findSpotBySizePreference(vSize, false);
        }

        if (chosen == null) {
            System.out.println("❌ Sorry! No parking spot available for " + vehicle.getType());
            return null;
        }

        ReentrantLock lock = manager.getLockForSpot(chosen.getId());
        boolean locked = false;
        for (int attempt = 0; attempt < 3; attempt++) {
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                locked = true;
                break;
            }
        }
        if (!locked) {
            System.out.println("⚠️ Spot " + chosen.getId() + " was busy after retries — skipping allocation.");
            return null;
        }


        try {
            if (!chosen.canFit(vSize)) {
                return null;
            }
            ParkingTicket ticket = TicketFactory.createTicket(chosen.getId(), vehicle.getLicensePlate(), vehicle.getType());
            chosen.occupy(ticket);
            System.out.println("✅ " + vehicle.getLicensePlate() +
                    " parked at spot " + chosen.getId() +
                    " (Ticket ID: " + ticket.getTicketId() + ")");
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    private ParkingSpot findSpotBySizePreference(int vehicleSize, boolean exactOnly) {

        List<ParkingFloor> floors = new ArrayList<>(manager.getAllFloors());
        floors.sort(Comparator.comparing(ParkingFloor::getFloorId));

        ParkingSpot candidate = null;
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots().values()) {
                if (spot == null) continue;
                if (exactOnly) {
                    if (spot.getSizeLevel() == vehicleSize && spot.canFit(vehicleSize)) {
                        return spot;
                    }
                } else {
                    if (spot.getSizeLevel() >= vehicleSize && spot.canFit(vehicleSize)) {
                        if (candidate == null || spot.getSizeLevel() < candidate.getSizeLevel()) {
                            candidate = spot;
                        }
                    }
                }
            }
        }
        return candidate;
    }
}


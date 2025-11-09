package main.java.smartparking;

import main.java.smartparking.manager.ParkingLotManager;
import main.java.smartparking.model.ParkingSpot;
import main.java.smartparking.model.ParkingFloor;
import main.java.smartparking.model.ParkingTicket;
import main.java.smartparking.model.Vehicle;
import main.java.smartparking.model.VehicleType;
import main.java.smartparking.service.BillingConfig;
import main.java.smartparking.service.BillingService;
import main.java.smartparking.service.SpotAllocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import java.util.*;
import java.util.concurrent.*;

public class SmartParkingApp {
    public static void main(String[] args) throws Exception {
        initializeParkingLot();

        SpotAllocationService allocator = new SpotAllocationService();

        // 💰 Configure billing (hourly with ₹50 per hour as example)
        BillingConfig config = new BillingConfig(
                BillingConfig.StrategyType.HOURLY,
                1.0, // multiplier
                1,   // min 1 hour charge
                0.0, // flat fee
                0.0  // discount
        );
        BillingService billing = new BillingService(config.createStrategy());

        System.out.println("\n==========================================");
        System.out.println("🚗 SINGLE VEHICLE CHECK-IN / CHECK-OUT DEMO");
        System.out.println("==========================================");

        Vehicle car1 = new Vehicle("CAR-100", VehicleType.CAR);
        ParkingTicket t1 = allocator.checkIn(car1);
        if (t1 != null) {
            Thread.sleep(2000); // simulate 2 seconds stay
            billing.checkout(t1);
        }

        System.out.println("\n==========================================");
        System.out.println("🚙 CONCURRENT VEHICLE CHECK-IN DEMO");
        System.out.println("==========================================");

        ExecutorService executor = Executors.newFixedThreadPool(6);
        List<Future<ParkingTicket>> futures = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            final int id = i;
            futures.add(executor.submit(() -> {
                Vehicle v = new Vehicle("CONC-" + id, (id % 3 == 0) ? VehicleType.BUS : VehicleType.CAR);
                return allocator.checkIn(v);
            }));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        List<ParkingTicket> tickets = new ArrayList<>();
        for (Future<ParkingTicket> f : futures) {
            try {
                ParkingTicket t = f.get();
                if (t != null) tickets.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n==========================================");
        System.out.println("💰 CONCURRENT VEHICLE CHECK-OUT DEMO");
        System.out.println("==========================================");

        for (ParkingTicket t : tickets) {
            Thread.sleep(100); // small delay to simulate staggered exits
            billing.checkout(t);
        }

        System.out.println("\n==========================================");
        System.out.println("🅿️ FINAL PARKING LOT AVAILABILITY");
        System.out.println("==========================================");
        printAvailability();
    }

    // 🏗️ Initialize Parking Floors & Spots
    private static void initializeParkingLot() {
        ParkingLotManager mgr = ParkingLotManager.getInstance();

        mgr.addSpotToFloor("F1", new ParkingSpot("F1-A1", 1));
        mgr.addSpotToFloor("F1", new ParkingSpot("F1-A2", 2));
        mgr.addSpotToFloor("F1", new ParkingSpot("F1-A3", 2));
        mgr.addSpotToFloor("F1", new ParkingSpot("F1-A4", 3));

        mgr.addSpotToFloor("F2", new ParkingSpot("F2-B1", 3));
        mgr.addSpotToFloor("F2", new ParkingSpot("F2-B2", 3));
        mgr.addSpotToFloor("F2", new ParkingSpot("F2-B3", 3));

        System.out.println("✅ Parking Lot Initialized with 2 Floors:");
        for (ParkingFloor floor : mgr.getAllFloors()) {
            System.out.println("  • Floor " + floor.getFloorId() + " has " + floor.getSpots().size() + " spots");
        }
    }

    // 🧾 Show Final Availability
    private static void printAvailability() {
        ParkingLotManager mgr = ParkingLotManager.getInstance();

        for (ParkingFloor floor : mgr.getAllFloors()) {
            System.out.println("Floor " + floor.getFloorId() + ":");

            for (ParkingSpot spot : floor.getSpots().values()) {
                System.out.printf("  %s - %s%n",
                        spot.getId(),
                        spot.isOccupied() ? "❌ Occupied" : "✅ Available");
            }

            long available = floor.getSpots().values().stream()
                    .filter(s -> !s.isOccupied())
                    .count();

            System.out.printf("Total available spots on %s: %d%n", floor.getFloorId(), available);
        }
    }
}

package main.java.smartparking.service;

import main.java.smartparking.manager.ParkingLotManager;
import main.java.smartparking.model.ParkingSpot;
import main.java.smartparking.model.ParkingTicket;
import main.java.smartparking.model.VehicleType;
import java.util.concurrent.locks.ReentrantLock;

public class BillingService {
    private final FeeStrategy feeStrategy;
    private final ParkingLotManager manager = ParkingLotManager.getInstance();

    public BillingService(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public double checkout(ParkingTicket ticket) {
        long exit = System.currentTimeMillis();
        VehicleType type = ticket.getVehicleType();
        double fee = feeStrategy.calculate(ticket.getEntryTime(), exit, type);

        ParkingSpot spot = manager.getSpot(ticket.getSpotId());
        if (spot == null) {
            System.out.println("❌ Checkout Failed: Spot not found for ticket " + ticket.getTicketId());
            return -1.0;
        }

        ReentrantLock lock = manager.getLockForSpot(spot.getId());
        lock.lock();
        try {
            ParkingTicket current = spot.getCurrentTicket();
            if (current == null || !current.getTicketId().equals(ticket.getTicketId())) {
                System.out.println("⚠️ Checkout Warning: Ticket mismatch or already vacated for " + ticket.getLicensePlate());
                ticket.setExit(exit, fee);
                return fee;
            }
            ticket.setExit(exit, fee);
            spot.vacate();

            double hours = (double) (exit - ticket.getEntryTime()) / (1000 * 60 * 60);

            System.out.println("\n--------------------------------------");
            System.out.println("🚗 Vehicle: " + ticket.getLicensePlate());
            System.out.println("🅿️  Spot: " + spot.getId());
            System.out.printf("⏱️  Duration: %.2f hours%n", hours);
            System.out.printf("💰 Fee: ₹%.2f%n", fee);
            System.out.println("--------------------------------------\n");

            return fee;
        } finally {
            lock.unlock();
        }
    }
}

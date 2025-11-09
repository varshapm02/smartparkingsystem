package main.java.smartparking.factory;

import main.java.smartparking.model.ParkingTicket;
import main.java.smartparking.model.VehicleType;

import java.util.concurrent.atomic.AtomicLong;

public class TicketFactory {
    private static final AtomicLong COUNTER = new AtomicLong(1000);

    public static ParkingTicket createTicket(String spotId, String licensePlate, VehicleType vehicleType) {
        String ticketId = "T-" + COUNTER.getAndIncrement();
        long entry = System.currentTimeMillis();
        return new ParkingTicket(ticketId, spotId, licensePlate, vehicleType, entry);
    }
}


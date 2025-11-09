package main.java.smartparking.model;
import java.time.Instant;
public class ParkingTicket {
    private final String ticketId;
    private final String spotId;
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final long entryTime;

    private volatile long exitTime;
    private volatile double feeAmount;

    public ParkingTicket(String ticketId, String spotId, String licensePlate, VehicleType vehicleType, long entryTime) {
        this.ticketId = ticketId;
        this.spotId = spotId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.exitTime = 0L;
        this.feeAmount = 0.0;
    }

    public String getTicketId() { return ticketId; }
    public String getSpotId() { return spotId; }
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getVehicleType() { return vehicleType; }
    public long getEntryTime() { return entryTime; }
    public long getExitTime() { return exitTime; }
    public double getFeeAmount() { return feeAmount; }

    public void setExit(long exitTime, double feeAmount) {
        this.exitTime = exitTime;
        this.feeAmount = feeAmount;
    }

    @Override
    public String toString() {
        return "Ticket{" + ticketId + "," + spotId + "," + licensePlate + "," + vehicleType + "," +
                "in=" + Instant.ofEpochMilli(entryTime) + (exitTime==0? "":",out="+Instant.ofEpochMilli(exitTime)) + "}";
    }
}

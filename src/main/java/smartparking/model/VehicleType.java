package main.java.smartparking.model;

// VehicleType.java
public enum VehicleType {
    MOTORCYCLE(1, 20.0),
    CAR(2, 50.0),
    BUS(3, 100.0);

    private final int sizeLevel;
    private final double hourlyRate;

    VehicleType(int sizeLevel, double hourlyRate) {
        this.sizeLevel = sizeLevel;
        this.hourlyRate = hourlyRate;
    }

    public int getSizeLevel() { return sizeLevel; }
    public double getHourlyRate() { return hourlyRate; }
}


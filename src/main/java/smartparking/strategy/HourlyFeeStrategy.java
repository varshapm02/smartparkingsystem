package main.java.smartparking.strategy;

import main.java.smartparking.model.VehicleType;
import main.java.smartparking.service.FeeStrategy;

public class HourlyFeeStrategy implements FeeStrategy {
    private final double hourlyMultiplier; // multiplier applied to vehicleType.getHourlyRate()
    private final int minimumHours;

    public HourlyFeeStrategy(double hourlyMultiplier, int minimumHours) {
        this.hourlyMultiplier = hourlyMultiplier;
        this.minimumHours = Math.max(1, minimumHours);
    }

    @Override
    public double calculate(long entryTimeMillis, long exitTimeMillis, VehicleType type) {
        long durationMillis = Math.max(0L, exitTimeMillis - entryTimeMillis);
        double hours = (double) durationMillis / (1000.0 * 60.0 * 60.0);
        long billed = (long) Math.ceil(hours);
        if (billed < minimumHours) billed = minimumHours;
        return billed * type.getHourlyRate() * hourlyMultiplier;
    }
}


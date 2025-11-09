package main.java.smartparking.strategy;

import main.java.smartparking.model.VehicleType;
import main.java.smartparking.service.FeeStrategy;

public class PerMinuteFeeStrategy implements FeeStrategy {
    private final double perMinuteRate;

    public PerMinuteFeeStrategy(double perMinuteRate) {
        this.perMinuteRate = perMinuteRate;
    }

    @Override
    public double calculate(long entryTimeMillis, long exitTimeMillis, VehicleType type) {
        long durationMillis = Math.max(0L, exitTimeMillis - entryTimeMillis);
        long minutes = (long) Math.ceil((double) durationMillis / (1000.0 * 60.0));
        return minutes * perMinuteRate * (type.getHourlyRate() / type.getHourlyRate());
    }
}


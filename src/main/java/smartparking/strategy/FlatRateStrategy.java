package main.java.smartparking.strategy;

import main.java.smartparking.model.VehicleType;
import main.java.smartparking.service.FeeStrategy;

public class FlatRateStrategy implements FeeStrategy {
    private final double flatAmount;

    public FlatRateStrategy(double flatAmount) {
        this.flatAmount = flatAmount;
    }

    @Override
    public double calculate(long entryTimeMillis, long exitTimeMillis, VehicleType type) {
        return flatAmount;
    }
}


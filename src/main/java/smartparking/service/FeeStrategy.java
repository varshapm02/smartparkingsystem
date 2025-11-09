package main.java.smartparking.service;

import main.java.smartparking.model.VehicleType;

public interface FeeStrategy {
    double calculate(long entryTimeMillis, long exitTimeMillis, VehicleType type);
}


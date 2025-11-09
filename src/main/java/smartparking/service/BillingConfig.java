package main.java.smartparking.service;

import main.java.smartparking.strategy.FlatRateStrategy;
import main.java.smartparking.strategy.HourlyFeeStrategy;
import main.java.smartparking.strategy.PerMinuteFeeStrategy;

public class BillingConfig {
    public enum StrategyType { HOURLY, PER_MINUTE, FLAT }

    private final StrategyType strategyType;


    private final double hourlyMultiplier;
    private final int minimumHours;
    private final double perMinuteRate;
    private final double flatAmount;

    public BillingConfig(StrategyType strategyType, double hourlyMultiplier, int minimumHours,
                         double perMinuteRate, double flatAmount) {
        this.strategyType = strategyType;
        this.hourlyMultiplier = hourlyMultiplier;
        this.minimumHours = minimumHours;
        this.perMinuteRate = perMinuteRate;
        this.flatAmount = flatAmount;
    }

    public FeeStrategy createStrategy() {
        switch (strategyType) {
            case HOURLY:
                return new HourlyFeeStrategy(hourlyMultiplier, minimumHours);
            case PER_MINUTE:
                return new PerMinuteFeeStrategy(perMinuteRate);
            case FLAT:
                return new FlatRateStrategy(flatAmount);
            default:
                return new HourlyFeeStrategy(1.0, 1);
        }
    }
}


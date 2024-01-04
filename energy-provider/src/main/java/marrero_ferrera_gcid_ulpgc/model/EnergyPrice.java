package marrero_ferrera_gcid_ulpgc.model;

import java.time.Instant;

public class EnergyPrice {
    private float price;
    private final Instant ts;
    private Instant predictionTime;
    private Slot slot;
    private final String ss;

    public EnergyPrice(float price, Instant predictionTime, Slot slot) {
        this.predictionTime = predictionTime;
        this.price = price;
        this.ts = Instant.now();
        this.slot = slot;
        this.ss = "EnergyPricesProvider";
    }

    public EnergyPrice() {
        this.ss = "EnergyPricesProvider";
        this.ts = Instant.now();
    }

    public EnergyPrice setSlot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public EnergyPrice setPrice(float price) {
        this.price = price;
        return this;
    }

    public EnergyPrice setPredictionTime(Instant predictionTime) {
        this.predictionTime = predictionTime;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public String getSs() {
        return ss;
    }

    public Instant getTs() {
        return ts;
    }

    public Slot getSlot() {
        return slot;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public enum Slot {
        Valley, Flat, Peak
    }
}

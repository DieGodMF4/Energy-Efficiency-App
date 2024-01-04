package marrero_ferrera_gcid_ulpgc.control.schemas;

import java.time.Instant;

public class EnergyPrice {
    private float price;
    private Instant ts;
    private Slot slot; //TODO
    private final String ss = "EnergyPricesProvider";

    public EnergyPrice() {
    }

    public EnergyPrice setSlot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public EnergyPrice setPrice(float price) {
        this.price = price;
        return this;
    }

    public EnergyPrice setTs(Instant ts) {
        this.ts = ts;
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
    } //TODO change

    public enum Slot {
        Valley, Flat, Peak
    }
}

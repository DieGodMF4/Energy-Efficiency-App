package marrero_ferrera_gcid_ulpgc.model;

import java.time.Instant;

public class EnergyPrice {
    private float price;
    private Instant ts;
    private State state;

    public EnergyPrice() {
    }

    public EnergyPrice setState(State state) {
        this.state = state;
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
        return "EnergyPricesProvider";
    }

    public long getTs() {
        return ts.getEpochSecond();
    }
}

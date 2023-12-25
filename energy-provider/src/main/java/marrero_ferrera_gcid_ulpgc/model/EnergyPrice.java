package marrero_ferrera_gcid_ulpgc.model;

public class EnergyPrice {
    private float price;
    private String ts;
    private State state;
    private static final String unit = "PVPC (€/MWh)";

    public EnergyPrice(float price, String ts, State state) {
        this.price = price;
        this.ts = ts;
        this.state = state;
    }

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

    public EnergyPrice setTs(String ts) {
        this.ts = ts;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public State getState() {
        return state;
    }
}

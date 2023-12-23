package marrero_ferrera_gcid_ulpgc.model;

public class EnergyPrice {
    private String price;
    private long ts;
    private final State state;
    private static final String unit = "PVPC (€/MWh)";

    public EnergyPrice(String price, long ts, State state) {
        this.price = price;
        this.ts = ts;
        this.state = state;
    }
}

package marrero_ferrera_gcid_ulpgc.model;

public class EnergyPrice {
    private float price;
    private String ts;
    private final State state;
    private static final String unit = "PVPC (€/MWh)";

    public EnergyPrice(float price, String ts, State state) {
        this.price = price;
        this.ts = ts;
        this.state = state;
    }
}

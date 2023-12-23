package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

import java.time.Instant;

public interface PriceSupplier {
    EnergyPrice getPrice(Instant ts);
}

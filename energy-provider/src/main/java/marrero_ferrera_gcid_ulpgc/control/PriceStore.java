package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

public interface PriceStore {
    void insertPrice(EnergyPrice energyPrice);
}

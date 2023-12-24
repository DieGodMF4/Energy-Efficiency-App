package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

import java.util.ArrayList;

public interface PriceSupplier {
    ArrayList<EnergyPrice> getPriceForToday() throws MySenderException;
}

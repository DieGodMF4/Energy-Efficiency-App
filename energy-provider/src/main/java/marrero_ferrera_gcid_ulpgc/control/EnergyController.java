package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

public class EnergyController {

    private final PriceSupplier supplier;
    private final PriceStore store;

    public EnergyController(PriceSupplier supplier, PriceStore store) {
        this.supplier = supplier;
        this.store = store;
    }

    public void execute() throws MySenderException {
        ArrayList<EnergyPrice> energyPrices = supplier.getPriceForToday();
        for (EnergyPrice price : energyPrices) store.insertPrice(price);
    }
}



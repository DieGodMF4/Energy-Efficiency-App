package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

public class EnergyController {

    public record DataContainer(String ss, long ts, long predTime, EnergyPrice energyPrice) {
    }
}

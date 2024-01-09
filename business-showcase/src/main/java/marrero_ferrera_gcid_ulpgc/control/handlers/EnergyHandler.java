package marrero_ferrera_gcid_ulpgc.control.handlers;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class EnergyHandler {
    private final Model model;

    public EnergyHandler(Model model) {
        this.model = model;
    }

    public void handle(EnergyPrice energyEvent) {
        Model.Item item = processEnergyProvider(energyEvent);

        if (!isItemDuplicate(item.getPredictionTime(), item.getPrice())) {
            model.addEnergyItem(item);
        }
    }

    private boolean isItemDuplicate(Instant predictionTime, float price) {
        ArrayList<Model.Item> items = model.getEnergyItems();
        return items.stream()
                .filter(item -> Objects.nonNull(item.getPrice()))
                .anyMatch(item -> item.getPredictionTime().equals(predictionTime) &&
                        Float.compare(item.getPrice(), price) == 0);
    }

    private Model.Item processEnergyProvider(EnergyPrice energyPrice) {
        Model.Item item = new Model.Item();
        item.setPredictionTime(energyPrice.getPredictionTime());
        item.setPrice(energyPrice.getPrice());
        item.setSlot(energyPrice.getSlot());
        return item;
    }
}

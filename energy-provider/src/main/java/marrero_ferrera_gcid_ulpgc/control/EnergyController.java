package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

public class EnergyController {

    public EnergyController() {
    }

    public void getAndPublishEnergyPrices(String topicName) throws MySenderException {
        ElectricEnergySupplier supplier = new ElectricEnergySupplier();
        ArrayList<EnergyPrice> energyPrices = supplier.getPriceForToday();
        for (EnergyPrice price : energyPrices) {
            DataContainer dataContainer = new DataContainer(price.getSs(), price.getTs(),
                    Instant.now().getEpochSecond(), price);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantSerializer())
                    .create();
            String dataContainerToJson = gson.toJson(dataContainer);
            JMSPriceStore store = new JMSPriceStore(topicName);
            store.insertPrice(dataContainerToJson);
        }
    }

    public record DataContainer(String ss, long ts, long takeTime, EnergyPrice energyPrice) {
    }

    static class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(instant.toString());
        }
    }
}



package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class ElectricEnergySupplier implements PriceSupplier {
    public ElectricEnergySupplier() {
    }

    @Override
    public ArrayList<EnergyPrice> getPriceForToday() throws MySenderException {
        ArrayList<EnergyPrice> prices = new ArrayList<>();
        try {
            JsonObject jsonObject = apiConnector();
            JsonArray valuesArray = jsonParsingToValues(jsonObject);
            if (valuesArray != null) {
                for (JsonElement valueElement : valuesArray) {
                    EnergyPrice price = new EnergyPrice();
                    price.setPrice(valueElement.getAsJsonObject().get("value").getAsFloat());
                    price.setPredictionTime(Instant.parse(valueElement.getAsJsonObject().get("datetime").getAsString()).plusSeconds(3600));
                    prices.add(price);
                }
                classifyPrices(prices);
                return prices;
            }
            throw new MySenderException("Location or time were incorrect!");
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    private void classifyPrices(List<EnergyPrice> originalPrices) {
        List<EnergyPrice> prices = new ArrayList<>(originalPrices);
        prices.sort((p1, p2) -> Float.compare(p1.getPrice(), p2.getPrice()));

        int totalPrices = prices.size();
        int lowEnd = 8;
        int midStart = totalPrices / 3;
        int midEnd = midStart + 8;

        for (int i = 0; i < totalPrices; i++) {
            EnergyPrice currentPrice = prices.get(i);
            EnergyPrice.Slot state;

            if (i < lowEnd) {
                state = EnergyPrice.Slot.Valley;
                currentPrice.setSlot(state);
            } else if (i < midEnd) {
                state = EnergyPrice.Slot.Flat;
                currentPrice.setSlot(state);
            } else {
                state = EnergyPrice.Slot.Peak;
                currentPrice.setSlot(state);
            }
            originalPrices.stream()
                    .filter(price -> price.getPrice() == currentPrice.getPrice())
                    .findFirst()
                    .ifPresent(price -> price.setSlot(state));
        }
    }

    private JsonObject apiConnector() {
        HttpClient httpClient = HttpClients.createDefault();
        String httpUrl = "https://apidatos.ree.es/es/datos/mercados/precios-mercados-tiempo-real?start_date=" +
                obtainInstantMidNight(LocalDate.now().plusDays(0)) + "&end_date=" +
                obtainInstantMidNight(LocalDate.now().plusDays(1)).plusSeconds(3600) + "&time_trunc=hour";
        HttpGet httpGet = new HttpGet(httpUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            JsonObject asJsonObject = new JsonParser().parse(json).getAsJsonObject();
            return asJsonObject;
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    private Instant obtainInstantMidNight(LocalDate date) {
        LocalDateTime midnight = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        return midnight.atZone(ZoneId.of("Europe/Madrid")).toInstant();
    }

    private JsonArray jsonParsingToValues(JsonObject jsonObject) throws MySenderException {
        JsonArray includedArray = jsonObject.getAsJsonArray("included");
        if (includedArray != null && !includedArray.isEmpty()) {
            JsonObject pvpcObject = includedArray.get(0).getAsJsonObject();
            JsonObject attributesObject = pvpcObject.getAsJsonObject("attributes");
            if (attributesObject != null) {
                return attributesObject.getAsJsonArray("values");
            }
        }
        throw new MySenderException("An error occurred while parsing.");
    }
}

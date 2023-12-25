package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.model.State;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;

public class ElectricEnergySupplier implements PriceSupplier {
    public ElectricEnergySupplier() {}

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
                    price.setTs(Instant.parse(valueElement.getAsJsonObject().get("datetime").getAsString()));
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

    private void classifyPrices(ArrayList<EnergyPrice> prices) {
        double minPrice = prices.stream().mapToDouble(EnergyPrice::getPrice).min().orElse(0.0);
        double maxPrice = prices.stream().mapToDouble(EnergyPrice::getPrice).max().orElse(0.0);

        double lowThreshold = minPrice + (maxPrice - minPrice) / 3;
        double highThreshold = minPrice + 2 * (maxPrice - minPrice) / 3;


        for (EnergyPrice price : prices) {
            double priceValue = price.getPrice();

            if (priceValue <= lowThreshold) {
                price.setState(State.Valley);
            } else if (priceValue <= highThreshold) {
                price.setState(State.Flat);
            } else {
                price.setState(State.Peak);
            }
        }
    }

    private JsonObject apiConnector() {
        HttpClient httpClient = HttpClients.createDefault();
        String httpUrl = "https://apidatos.ree.es/es/datos/mercados/precios-mercados-tiempo-real?start_date=" +
                obtainInstantMidNight(LocalDate.now()) + "&end_date=" +
                obtainInstantMidNight(LocalDate.now().plusDays(1)) + "&time_trunc=hour";
        HttpGet httpGet = new HttpGet(httpUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            return new com.google.gson.JsonParser().parse(json).getAsJsonObject();
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

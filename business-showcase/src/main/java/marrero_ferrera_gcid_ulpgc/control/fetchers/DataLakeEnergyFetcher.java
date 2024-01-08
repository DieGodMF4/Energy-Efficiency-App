package marrero_ferrera_gcid_ulpgc.control.fetchers;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.control.MyManagerException;
import marrero_ferrera_gcid_ulpgc.control.handlers.EnergyHandler;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class DataLakeEnergyFetcher implements DataLakeFetcher {
    private final EnergyHandler handler;
    private final String additionalPath;
    private final String topicName;
    private final String ss;
    private final Instant date;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new DataLakeEnergyFetcher.InstantSerializer())
            .create();

    public DataLakeEnergyFetcher(String topicName, Instant date, EnergyHandler handler, String additionalPath) {
        this.handler = handler;
        this.topicName = topicName;
        this.ss = "EnergyPricesProvider";
        this.date = date;
        this.additionalPath = additionalPath;
    }

    @Override
    public void fetchFiles() throws MyManagerException {
        String filePath = buildFinalFilePath();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                handler.handle(gson.fromJson(line, EnergyPrice.class));
            }
        } catch (IOException e) {
            throw new MyManagerException("An error occurred while searching the file", e);
        }
    }

    @Override
    public boolean fileExists() throws MyManagerException {
        String filePath = buildFinalFilePath();
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            Set<Instant> distinctPredictionTimes = new HashSet<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null && distinctPredictionTimes.size() < 10) {
                    parseAndAddPredictionTime(line, distinctPredictionTimes);
                }
            } catch (IOException e) {
                throw new MyManagerException("Error occurred while looking for the file", e);
            }
            return distinctPredictionTimes.size() >= 10;
        }
        return false;
    }

    private static void parseAndAddPredictionTime(String line, Set<Instant> distinctPredictionTimes) {
        JsonElement jsonElement = JsonParser.parseString(line);
        Instant predictionTime = Instant.parse(jsonElement.getAsJsonObject().get("predictionTime").getAsString());
        distinctPredictionTimes.add(predictionTime);
    }

    private String buildFinalFilePath() {
        String basePath = "datalake/eventstore";
        String suitableDate = formatDate(date);
        String fileName = String.format("%s/%s/%s/%s.events", additionalPath, topicName, ss, suitableDate);
        return String.join(File.separator, basePath, fileName);
    }

    private String formatDate(Instant date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    static class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.toString());
        }

        @Override
        public Instant deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Instant.parse(jsonElement.getAsString());
        }
    }
}

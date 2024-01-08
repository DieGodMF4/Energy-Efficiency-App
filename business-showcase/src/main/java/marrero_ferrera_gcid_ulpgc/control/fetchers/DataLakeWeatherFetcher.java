package marrero_ferrera_gcid_ulpgc.control.fetchers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import marrero_ferrera_gcid_ulpgc.control.MyManagerException;
import marrero_ferrera_gcid_ulpgc.control.handlers.WeatherHandler;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;

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

public class DataLakeWeatherFetcher implements DataLakeFetcher {
    private final WeatherHandler handler;
    private final String additionalPath;
    private final String topicName;
    private final String ss;
    private final Instant date;

    public DataLakeWeatherFetcher(String topicName, Instant date, WeatherHandler handler, String additionalPath) {
        this.handler = handler;
        this.topicName = topicName;
        this.ss = "WeatherProvider";
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
                handler.handle(new Gson().fromJson(line, Weather.class));
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
}

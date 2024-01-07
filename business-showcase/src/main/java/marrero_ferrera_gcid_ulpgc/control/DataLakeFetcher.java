package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class DataLakeFetcher implements Fetcher {
    private final JsonOperator operator;
    private String additionalPath;
    private final String topicName;
    private final String ss;
    private final Instant date;
    private final ArrayList<String> results;

    public DataLakeFetcher(String topicName, String ss, Instant date, JsonOperator operator) {
        this.operator = operator;
        this.additionalPath = "";
        this.topicName = topicName;
        this.ss = ss;
        this.date = date;
        this.results = new ArrayList<>();
    }

    public DataLakeFetcher setAdditionalPath(String additionalPath) {
        this.additionalPath = additionalPath;
        return this;
    }

    @Override
    public void fetchFiles() throws MyManagerException {
        String filePath = buildFinalFilePath();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonElement jsonElement = JsonParser.parseString(line);
                results.add(jsonElement.getAsString());
            }
            operator.operateAsFetcher(results);
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
                    JsonElement jsonElement = JsonParser.parseString(line);
                    Instant predictionTime = Instant.parse(jsonElement.getAsJsonObject().get("predictionTime").getAsString());
                    distinctPredictionTimes.add(predictionTime);
                    // EXTRACTABLE METHOD
                }
            } catch (IOException e) {
                throw new MyManagerException("Error occurred while looking for the file", e);
            }
            return distinctPredictionTimes.size() >= 10;
        }
        return false;
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

    public ArrayList<String> getResults() {
        return results;
    }
}

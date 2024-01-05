package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataLakeFetcher implements Fetcher {
    private String additionalPath;
    private final String topicName;
    private final String ss;
    private final String date;

    public DataLakeFetcher(String topicName, String ss, String date) {
        this.additionalPath = "";
        this.topicName = topicName;
        this.ss = ss;
        this.date = date;
    }

    public DataLakeFetcher setAdditionalPath(String additionalPath) {
        this.additionalPath = additionalPath;
        return this;
    }

    @Override
    public void fetchFilesJson() {
        String filePath = buildFinalFilePath();

        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parsear cada línea como un objeto JSON
                JsonElement jsonElement = JsonParser.parseString(line);
                System.out.println(jsonElement);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejo adecuado de excepciones
        }
        // return new Model(predTimeEnergy, "energyMarketPrices", "weatherProvider", weatherType, wind, clouds, price, slot);
    }

    private String buildFinalFilePath() {
        String basePath = "datalake/eventstore";
        String suitableDate = formatDate(date);
        String fileName = String.format("%s/%s/%s/%s.events", additionalPath, topicName, ss, suitableDate);
        return String.join(File.separator, basePath, fileName);
    }

    private String formatDate(String date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(date), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}

package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileEventStoreBuilder implements EventStoreBuilder {
    private final String basePath;
    private final String topicName;

    public FileEventStoreBuilder(String basePath, String topicName) {
        this.basePath = basePath;
        this.topicName = topicName;
    }

    @Override
    public void storeMessage(String jsonString) throws MyReceiverException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String source = getSource(jsonObject);
        String date;
        try {
            date = extractDate(jsonObject);
        } catch (ParseException e) {
            throw new MyReceiverException("Error occurred while parsing", e);
        }
        String directoryPath = basePath + topicName + File.separator + source + File.separator;
        String fileName = date + ".events";
        String fullPath = Paths.get(directoryPath, fileName).toString();
        writeJsonToFile(fullPath, jsonString);
}

    private void writeJsonToFile(String filePath, String jsonString) throws MyReceiverException {
        try {
            Path directoryPath = Paths.get(filePath).getParent();
            Files.createDirectories(directoryPath);
            File file = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.write(jsonString+"\n");
            }
        } catch (IOException e) {
            throw new MyReceiverException("An error occurred while introducing the Strings into the File.", e);
        }
    }

    private String getSource(JsonObject jsonObject) {
        return jsonObject.get("ss").getAsString();
    }

    private String extractDate(JsonObject jsonNode) throws ParseException {
        long tsValue = jsonNode.get("predictionTime").getAsLong();
        Date date = new Date(tsValue);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }
}


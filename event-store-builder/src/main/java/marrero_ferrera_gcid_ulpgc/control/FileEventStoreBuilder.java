package marrero_ferrera_gcid_ulpgc.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileEventStoreBuilder implements EventStoreBuilder {

    private final String basePath;

    public FileEventStoreBuilder(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void storeMessages(ArrayList<String> jsonStrings) throws MyReceiverException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (String jsonString : jsonStrings) {
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(jsonString);
            } catch (JsonProcessingException e) {
                throw new MyReceiverException("An error occurred while parsing the json String.", e);
            }

            String source = getSource(jsonNode);
            String date = extractDate(jsonNode);

            String directoryPath = basePath + source + "/";
            String fileName = date + ".events";
            String fullPath = Paths.get(directoryPath, fileName).toString();

            writeJsonToFile(fullPath, jsonString);
        }
    }

    private void writeJsonToFile(String filePath, String jsonString) throws MyReceiverException {
        try {
            Path directoryPath = Paths.get(filePath).getParent();
            Files.createDirectories(directoryPath);

            File file = new File(filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                if (file.length() > 0) {
                    writer.newLine();
                }

                objectMapper.writeValue(writer, jsonString);
            }
        } catch (IOException e) {
            throw new MyReceiverException("An error occurred while introducing the Strings into the File.", e);
        }
    }

    private String getSource(JsonNode jsonNode) {
        return jsonNode.get("ss").asText();
    }

    private String extractDate(JsonNode jsonNode) {
        long tsValue = jsonNode.get("ts").asLong();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date(tsValue * 1000L));
    }
}


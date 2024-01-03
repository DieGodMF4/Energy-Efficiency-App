package marrero_ferrera_gcid_ulpgc.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JSON Data App");

        // Crear un ComboBox para las horas del día
        ObservableList<String> hours = FXCollections.observableArrayList(
                "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
        );
        Scene scene = getScene(hours);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene getScene(ObservableList<String> hours) {
        ComboBox<String> hourComboBox = new ComboBox<>(hours);

        // Manejar el evento de selección del ComboBox
        hourComboBox.setOnAction(e -> {
            String selectedHour = hourComboBox.getValue();
            // Lógica para procesar la información JSON para la hora seleccionada
            String jsonData = readJsonFromFile("datalake/eventstore/prediction.Weather/WeatherProvider/20231225.events"); // Reemplaza "data.json" con el nombre de tu archivo
            displayInfoForHour(jsonData);
        });

        VBox vbox = new VBox(hourComboBox);
        return new Scene(vbox, 300, 200);
    }

    private String readJsonFromFile(String filename) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción de manera adecuada en tu aplicación
        }
        return jsonData.toString();
    }

    private void displayInfoForHour(String jsonData) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();

        // Obtener el objeto "weather" dentro de la estructura JSON
        JsonObject weatherObject = jsonObject.getAsJsonObject("weather");

        // Obtener la información específica del objeto "weather"
        String timestamp = weatherObject.get("ts").getAsString();
        String weatherType = weatherObject.get("weatherType").getAsString();
        double temperature = weatherObject.get("temperature").getAsDouble();
        double humidity = weatherObject.get("humidity").getAsDouble();
        double windSpeed = weatherObject.get("wind").getAsDouble();

        // Imprimir la información
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Weather Type: " + weatherType);
        System.out.println("Temperature: " + temperature + " °C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Wind Speed: " + windSpeed + " m/s");

    }
}


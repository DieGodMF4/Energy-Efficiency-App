package marrero_ferrera_gcid_ulpgc.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import marrero_ferrera_gcid_ulpgc.control.Main;

public class View extends Application {
    private static String url;
    private static String topicNameWeather;
    private static String topicNameEnergy;
    private static float powerChargeSolar;
    private static float powerChargeWind;
    private static boolean recommendedHalfBattery;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App Settings");
        GridPane grid = setGridPane();
        TextField urlField = new TextField("tcp://localhost:61616");
        TextField topicNameWeatherField = new TextField("prediction.Weather");
        TextField topicNameEnergyField = new TextField("energy.MarketPrices");
        TextField powerChargeSolarField = new TextField("0.0");
        TextField powerChargeWindField = new TextField("0.0");
        CheckBox recommendedHalfBatteryCheckbox = new CheckBox("Recommended Half Battery");

        addTextToGrids(grid, urlField, topicNameWeatherField, topicNameEnergyField, powerChargeSolarField,
                powerChargeWindField, recommendedHalfBatteryCheckbox);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            url = urlField.getText();
            topicNameWeather = topicNameWeatherField.getText();
            topicNameEnergy = topicNameEnergyField.getText();
            powerChargeSolar = Float.parseFloat(powerChargeSolarField.getText());
            powerChargeWind = Float.parseFloat(powerChargeWindField.getText());
            recommendedHalfBattery = recommendedHalfBatteryCheckbox.isSelected();

            new Thread(() -> Main.updateUserValues(url, topicNameWeather, topicNameEnergy, powerChargeSolar,
                    powerChargeWind, recommendedHalfBattery)).start();
            showResultsWindow(url, topicNameWeather, topicNameEnergy, powerChargeSolar, powerChargeWind, recommendedHalfBattery);
        });
        submitButtonAdder(primaryStage, grid, submitButton);
    }

    private static void submitButtonAdder(Stage primaryStage, GridPane grid, Button submitButton) {
        grid.add(submitButton, 0, 6, 2, 1);
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static GridPane setGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        return grid;
    }

    private static void addTextToGrids(GridPane grid, TextField urlField, TextField topicNameWeatherField, TextField topicNameEnergyField, TextField powerChargeSolarField, TextField powerChargeWindField, CheckBox recommendedHalfBatteryCheckbox) {
        grid.add(new Label("URL:"), 0, 0);
        grid.add(urlField, 1, 0);
        grid.add(new Label("Topic Name Weather:"), 0, 1);
        grid.add(topicNameWeatherField, 1, 1);
        grid.add(new Label("Topic Name Energy:"), 0, 2);
        grid.add(topicNameEnergyField, 1, 2);
        grid.add(new Label("Power Charge Solar:"), 0, 3);
        grid.add(powerChargeSolarField, 1, 3);
        grid.add(new Label("Power Charge Wind:"), 0, 4);
        grid.add(powerChargeWindField, 1, 4);
        grid.add(recommendedHalfBatteryCheckbox, 0, 5, 2, 1);
    }

    private void showResultsWindow(String url, String topicNameWeather, String topicNameEnergy,
                                   float powerChargeSolar, float powerChargeWind, boolean recommendedHalfBattery) {
        Stage resultsStage = new Stage();
        resultsStage.setTitle("Weather App Results");
        GridPane grid = setGridPane();
        extractedGridInfo(url, topicNameWeather, topicNameEnergy, powerChargeSolar, powerChargeWind, recommendedHalfBattery, grid);
        Scene scene = new Scene(grid, 400, 200);
        resultsStage.setScene(scene);
        resultsStage.show();
    }

    private static void extractedGridInfo(String url, String topicNameWeather, String topicNameEnergy, float powerChargeSolar, float powerChargeWind, boolean recommendedHalfBattery, GridPane grid) {
        grid.add(new Label("URL:"), 0, 0);
        grid.add(new Label(url), 1, 0);
        grid.add(new Label("Topic Name Weather:"), 0, 1);
        grid.add(new Label(topicNameWeather), 1, 1);
        grid.add(new Label("Topic Name Energy:"), 0, 2);
        grid.add(new Label(topicNameEnergy), 1, 2);
        grid.add(new Label("Power Charge Solar:"), 0, 3);
        grid.add(new Label(String.valueOf(powerChargeSolar)), 1, 3);
        grid.add(new Label("Power Charge Wind:"), 0, 4);
        grid.add(new Label(String.valueOf(powerChargeWind)), 1, 4);
        grid.add(new Label("Recommended Half Battery:"), 0, 5);
        grid.add(new Label(String.valueOf(recommendedHalfBattery)), 1, 5);
    }
}

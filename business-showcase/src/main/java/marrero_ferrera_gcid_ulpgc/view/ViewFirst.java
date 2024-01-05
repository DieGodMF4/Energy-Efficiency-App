package marrero_ferrera_gcid_ulpgc.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import marrero_ferrera_gcid_ulpgc.control.Main;

public class ViewFirst extends Application {
    private static String url;
    private static String topicNameWeather;
    private static String topicNameEnergy;
    private static float powerChargeSolar;
    private static float powerChargeWind;
    private static float batteryCapacity;
    private static boolean recommendedHalfBattery;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My Energy Inputs");
        GridPane grid = setGridPane();
        TextField urlField = new TextField("tcp://localhost:61616");
        TextField topicNameWeatherField = new TextField("prediction.Weather");
        TextField topicNameEnergyField = new TextField("energy.MarketPrices");
        TextField powerChargeSolarField = new TextField("0.0");
        TextField powerChargeWindField = new TextField("0.0");
        TextField batteryCapacityField = new TextField("0.0");
        CheckBox recommendedHalfBatteryCheckbox = new CheckBox("Want to stop at 50% battery capacity?");

        addTextToGrids(grid, urlField, topicNameWeatherField, topicNameEnergyField, powerChargeSolarField,
                powerChargeWindField, batteryCapacityField, recommendedHalfBatteryCheckbox);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            url = urlField.getText();
            topicNameWeather = topicNameWeatherField.getText();
            topicNameEnergy = topicNameEnergyField.getText();
            powerChargeSolar = Float.parseFloat(powerChargeSolarField.getText());
            powerChargeWind = Float.parseFloat(powerChargeWindField.getText());
            batteryCapacity = Float.parseFloat(batteryCapacityField.getText());
            recommendedHalfBattery = recommendedHalfBatteryCheckbox.isSelected();

            new Thread(() -> Main.updateUserValues(url, topicNameWeather, topicNameEnergy, powerChargeSolar,
                    powerChargeWind, batteryCapacity, recommendedHalfBattery)).start();
        });
        submitButtonAdder(primaryStage, grid, submitButton);
    }

    private static void submitButtonAdder(Stage primaryStage, GridPane grid, Button submitButton) {
        grid.add(submitButton, 0, 7, 2, 1);
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

    private static void addTextToGrids(GridPane grid, TextField urlField, TextField topicNameWeatherField,
                                       TextField topicNameEnergyField, TextField powerChargeSolarField, TextField powerChargeWindField,
                                       TextField batteryCapacityField, CheckBox recommendedHalfBatteryCheckbox) {
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
        grid.add(new Label("Your Battery Capacity:"), 0, 5);
        grid.add(batteryCapacityField, 1, 5);
        grid.add(recommendedHalfBatteryCheckbox, 0, 6, 2, 1);
    }

    private static void extractedGridInfo(String url, String topicNameWeather, String topicNameEnergy, float powerChargeSolar, float powerChargeWind, boolean recommendedHalfBattery, GridPane grid) {
        grid.add(new Label("Broker URL:"), 0, 0);
        grid.add(new Label(url), 1, 0);
        grid.add(new Label("Topic Name Weather:"), 0, 1);
        grid.add(new Label(topicNameWeather), 1, 1);
        grid.add(new Label("Topic Name Energy:"), 0, 2);
        grid.add(new Label(topicNameEnergy), 1, 2);
        grid.add(new Label("Power Charge Solar:"), 0, 3);
        grid.add(new Label(String.valueOf(powerChargeSolar)), 1, 3);
        grid.add(new Label("Power Charge Wind:"), 0, 4);
        grid.add(new Label(String.valueOf(powerChargeWind)), 1, 4);
        grid.add(new Label("Your Battery Capacity:"), 0, 5);
        grid.add(new Label(String.valueOf(powerChargeWind)), 1, 5);
        grid.add(new Label("Want to stop at 50% battery capacity?:"), 0, 6);
        grid.add(new Label(String.valueOf(recommendedHalfBattery)), 1, 6);
    }
}

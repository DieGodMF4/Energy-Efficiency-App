package marrero_ferrera_gcid_ulpgc.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import marrero_ferrera_gcid_ulpgc.control.Main;
import marrero_ferrera_gcid_ulpgc.control.MyManagerException;

public class ViewFirst extends Application {
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
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

        TextField urlField = createTextFieldWithTooltip("tcp://localhost:61616", "Enter the desired Broker URL for the ActiveMQ Connection");
        TextField topicNameWeatherField = createTextFieldWithTooltip("prediction.Weather", "Enter Topic Name for the Weather provider");
        TextField topicNameEnergyField = createTextFieldWithTooltip("energy.MarketPrices", "Enter Topic Name for the Energy Prices provider");
        TextField powerChargeSolarField = createTextFieldWithTooltip("0.0", "What is your solar panels total power? (Leave it if you have none)");
        TextField powerChargeWindField = createTextFieldWithTooltip("0.0", "What is your wind-generator total power? (Leave it if you have none)");
        TextField batteryCapacityField = createTextFieldWithTooltip("0.0", "Enter your battery capacity.");
        CheckBox recommendedHalfBatteryCheckbox = new CheckBox("Want to stop at 50% battery capacity? (Extends useful battery life)");

        addTextToGrids(grid, urlField, topicNameWeatherField, topicNameEnergyField,
                powerChargeSolarField, powerChargeWindField, batteryCapacityField, recommendedHalfBatteryCheckbox);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            url = urlField.getText();
            topicNameWeather = topicNameWeatherField.getText();
            topicNameEnergy = topicNameEnergyField.getText();
            powerChargeSolar = Float.parseFloat(powerChargeSolarField.getText());
            powerChargeWind = Float.parseFloat(powerChargeWindField.getText());
            batteryCapacity = Float.parseFloat(batteryCapacityField.getText());
            recommendedHalfBattery = recommendedHalfBatteryCheckbox.isSelected();

        });
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        submitButtonAdder(primaryStage, grid, submitButton);
    }

    private static void submitButtonAdder(Stage primaryStage, GridPane grid, Button submitButton) {
        grid.add(submitButton, 0, 9, 2, 1);
        Scene scene = new Scene(grid, 500, 400);
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
                                       TextField topicNameEnergyField, TextField powerChargeSolarField,
                                       TextField powerChargeWindField, TextField batteryCapacityField,
                                       CheckBox recommendedHalfBatteryCheckbox) {
        Label infoLabel = new Label("Enter your information: (Place your cursor on the fillable fields for more info)");
        grid.add(infoLabel, 0, 0, 2, 1);
        grid.add(new Label("Broker URL:"), 0, 1);
        grid.add(urlField, 1, 1);
        grid.add(new Label("Topic Name Weather:"), 0, 2);
        grid.add(topicNameWeatherField, 1, 2);
        grid.add(new Label("Topic Name Energy:"), 0, 3);
        grid.add(topicNameEnergyField, 1, 3);
        grid.add(new Label("Power Charge Solar: (KW)"), 0, 4);
        grid.add(powerChargeSolarField, 1, 4);
        grid.add(new Label("Power Charge Wind: (KW)"), 0, 5);
        grid.add(powerChargeWindField, 1, 5);
        grid.add(new Label("Your Battery Capacity: (KWh)"), 0, 6);
        grid.add(batteryCapacityField, 1, 6);
        grid.add(recommendedHalfBatteryCheckbox, 0, 7, 2, 1);
    }

    private TextField createTextFieldWithTooltip(String defaultValue, String tooltipText) {
        TextField textField = new TextField(defaultValue);
        Tooltip tooltip = new Tooltip(tooltipText);
        textField.setTooltip(tooltip);
        return textField;
    }
}

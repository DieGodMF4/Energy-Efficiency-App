package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.control.fetchers.DataLakeEnergyFetcher;
import marrero_ferrera_gcid_ulpgc.control.fetchers.DataLakeFetcher;
import marrero_ferrera_gcid_ulpgc.control.fetchers.DataLakeWeatherFetcher;
import marrero_ferrera_gcid_ulpgc.control.handlers.EnergyHandler;
import marrero_ferrera_gcid_ulpgc.control.handlers.WeatherHandler;
import marrero_ferrera_gcid_ulpgc.control.subscribers.EnergySubscriber;
import marrero_ferrera_gcid_ulpgc.control.subscribers.WeatherSubscriber;
import marrero_ferrera_gcid_ulpgc.model.Model;
import marrero_ferrera_gcid_ulpgc.view.ViewSwing;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.swing.*;
import java.time.Instant;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws MyManagerException {
        float powerChargeSolar = (args.length > 0) ? Float.parseFloat(args[0]) : 0.0f;
        float powerChargeWind = (args.length > 1) ? Float.parseFloat(args[1]) : 0.0f;
        float batteryCapacity = (args.length > 2) ? Float.parseFloat(args[2]) : 0.0f;
        boolean recommendedHalfBattery = args.length > 3 && Boolean.parseBoolean(args[3]);
        String topicNameWeather = (args.length > 4) ? (args[4]) : "prediction.Weather";
        String topicNameEnergy = (args.length > 5) ? (args[5]) : "energy.MarketPrices";
        String url = (args.length > 6) ? (args[6]) : "tcp://localhost:61616";
        String additionalPath = (args.length > 7) ? (args[7]) : "";

        Model model = new Model();
        model.setRenewableFields(powerChargeSolar, powerChargeWind, batteryCapacity, recommendedHalfBattery);
        WeatherHandler weatherHandler = new WeatherHandler(model);
        EnergyHandler energyHandler = new EnergyHandler(model);
        DataLakeFetcher weatherDataLakeFetcher = new DataLakeWeatherFetcher(topicNameWeather, Instant.now(), weatherHandler, additionalPath);
        DataLakeFetcher energyDataLakeFetcher = new DataLakeEnergyFetcher(topicNameEnergy, Instant.now(), energyHandler, additionalPath);

        System.out.println("Running app...");
        Session session;
        try {
            session = buildSession(url);
        } catch (JMSException e) {
            throw new MyManagerException("An error occurred while creating the session.", e);
        }
        startSubscribers(session, topicNameWeather, weatherHandler, topicNameEnergy, energyHandler);

        if (weatherDataLakeFetcher.fileExists()) weatherDataLakeFetcher.fetchFiles();
        else System.out.println("Weather File does not exist or is inaccessible Waiting for Weather Subscriber");
        if (energyDataLakeFetcher.fileExists()) energyDataLakeFetcher.fetchFiles();
        else System.out.println("Energy File does not exist or is inaccessible. Waiting for Energy Subscriber");

        addWeatherFields(model);
        ViewSwing view = new ViewSwing(model);
        viewFinalTable(view);
    }

    private static void startSubscribers(Session session, String topicNameWeather, WeatherHandler weatherHandler, String topicNameEnergy, EnergyHandler energyHandler) {
        new WeatherSubscriber(session, topicNameWeather, weatherHandler).start();
        new EnergySubscriber(session, topicNameEnergy, energyHandler).start();
    }

    private static Session buildSession(String url) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("client-ID");
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private static void viewFinalTable(ViewSwing tableUI) {
        tableUI.setTitle("Energy - Efficiency app");
        tableUI.setSize(800, 600);
        tableUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableUI.setVisible(true);
    }

    private static void addWeatherFields(Model model) {
        for (Model.Item energyItem: model.getFinalItems()) {
            boolean weatherExists = model.getWeatherItems().stream()
                    .map(Model.Item::getPredictionTime)
                    .anyMatch(instant -> instant.equals(energyItem.getPredictionTime()));
            if (weatherExists) insertWeatherFields(energyItem, model.getWeatherItems());
        }
    }

    private static void insertWeatherFields(Model.Item energyItem, ArrayList<Model.Item> weatherItems) {
        for (Model.Item weatherItem : weatherItems) {
            if (energyItem.getPredictionTime().equals(weatherItem.getPredictionTime())) {
                energyItem.setWeatherType(weatherItem.getWeatherType());
                energyItem.setWindGained(weatherItem.getWindGained());
                energyItem.setSolarGained(weatherItem.getSolarGained());
                energyItem.setBatteryGained(weatherItem.getBatteryGained());
            }
        }
    }

}

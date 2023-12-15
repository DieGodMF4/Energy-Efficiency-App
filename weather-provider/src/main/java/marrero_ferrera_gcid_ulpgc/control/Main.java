package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Location;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <API Key> <topic Name>");
            System.exit(1);
        }

        String apiKey = args[0];
        String topicName = args[1];

        List<Location> locationList = Arrays.asList(
        new Location(28.01f, -15.58f, "Gran Canaria", "Risco Prieto"),
        new Location(29.234f, -13.5f, "La Graciosa", "Caleta del Sebo"),
        new Location(29f,  -13.5f, "Lanzarote", "Costa Teguise"),
        new Location(28.35f, -14.11f, "Fuerteventura", "Pájara"),
        new Location(28.1f,  -17.12f, "La Gomera", "San Sebastián de La Gomera"),
        new Location(28.8f,  -17.81f, "La Palma", "Bosque de Los Tilos"),
        new Location(27.73f,  -18.05f, "El Hierro", "El Pinar"),
        new Location(28.28f,  -16.64f, "Tenerife", "Parque Nacional del Teide"));

        System.out.println("Task started and scheduled... \n(Ignore errors)");

        Timer timer = new Timer();
        short delay = 0;
        long period = 6 * 60 * 60 * 1000;

        for (Location location: locationList) {
            timer.scheduleAtFixedRate(new Task(location, apiKey, topicName), delay, period);
        }
    }
}
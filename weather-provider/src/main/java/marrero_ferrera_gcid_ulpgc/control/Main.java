package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Location;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Arguments: java Main <API Key> <topic Name> <latitude> <longitude>");
            System.out.println("(You can optionally introduce a NAME for your location as 5th argument).");
            System.exit(1);
        }
        String apiKey = args[0];
        String topicName = args[1];
        float latitude = Float.parseFloat(args[2]);
        float longitude = Float.parseFloat(args[3]);
        String ubiName = (args.length > 4) ? args[4] : "My Home";

        Location location = new Location(latitude, longitude, ubiName);
        System.out.println("Task started and scheduled... \n(Ignore errors)");

        Timer timer = new Timer();
        short delay = 0;
        long period = 6 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(new Task(location, apiKey, topicName), delay, period);
    }
}
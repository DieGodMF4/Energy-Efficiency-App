package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("Arguments: java Main <API Key> <broker url> <topic Name> <latitude> <longitude>");
            System.out.println("(You can optionally introduce a NAME for your location as 6th argument).");
            System.exit(1);
        }
        String apiKey = args[0];
        String url = args[1];
        String topicName = args[2];
        float latitude = Float.parseFloat(args[3]);
        float longitude = Float.parseFloat(args[4]);
        String ubiName = (args.length > 5) ? args[5] : "My Home";
        Weather.Location location = new Weather.Location(latitude, longitude, ubiName);
        System.out.println("Task started and scheduled... \n(Ignore errors)");
        Timer timer = new Timer();
        short delay = 0;
        long period = 6 * 60 * 60 * 1000;
        WeatherController controller = new WeatherController(new OpenWeatherMapSupplier(apiKey), new JMSWeatherStore(url, topicName), location);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                controller.execute();
            }
        }, delay, period);
    }
}
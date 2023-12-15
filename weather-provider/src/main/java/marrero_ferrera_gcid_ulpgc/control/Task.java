package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Location;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

public class Task extends TimerTask {
    private final Location location;
    private final String  apiKey;
    private final String topicName;
    public Task(Location location, String apiKey, String topicName) {
        this.location = location;
        this.apiKey = apiKey;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime scheduledTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0);

        if (now.isAfter(scheduledTime)) {
            scheduledTime = scheduledTime.plusDays(1);
        }

        WeatherController controller = new WeatherController();

        for (int i = 0; i < 5; i++) {
            LocalDateTime nextExecutionTime = scheduledTime.plusDays(i);
            Instant instant = nextExecutionTime.atZone(ZoneId.systemDefault()).toInstant();

            controller.getAndPublishWeatherData(location, apiKey, topicName, instant);
        }
    }
}

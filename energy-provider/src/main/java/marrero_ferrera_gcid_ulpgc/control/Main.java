package marrero_ferrera_gcid_ulpgc.control;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String topicName = (args.length > 0) ? args[0] : "energy.MarketPrices";

        Timer timer = new Timer();
        System.out.println("Task started and scheduled... \n(Ignore errors)");
        Task task = new Task(topicName);

        timer.schedule(task, 0);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long initialDelay = calculateInitialDelay();
        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    private static long calculateInitialDelay() {
        TimeZone europeMadridTimeZone = TimeZone.getTimeZone("Europe/Madrid");
        Calendar now = Calendar.getInstance(europeMadridTimeZone);

        now.set(Calendar.HOUR_OF_DAY, 20);
        now.set(Calendar.MINUTE, 30);
        now.set(Calendar.SECOND, 0);
        long initialDelay = now.getTimeInMillis() - System.currentTimeMillis();

        if (initialDelay < 0) {
            initialDelay += TimeUnit.DAYS.toMillis(1);
        }

        return initialDelay;
    }
}

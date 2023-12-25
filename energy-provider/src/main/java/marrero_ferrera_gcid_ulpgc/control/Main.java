package marrero_ferrera_gcid_ulpgc.control;

import java.util.Timer;

public class Main {
    public static void main(String[] args)  {
        String topicName = (args.length > 0) ? args[0] : "energy.MarketPrices";

        Timer timer = new Timer();
        short delay = 0;
        long period = 24 * 60 * 60 * 1000;
        System.out.println("Task started and scheduled... \n(Ignore errors)");

        timer.scheduleAtFixedRate(new Task(topicName), delay, period);
    }
}

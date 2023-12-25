package marrero_ferrera_gcid_ulpgc.control;

import java.util.TimerTask;

public class Task extends TimerTask {
    private final String topicName;

    public Task(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void run() {
        EnergyController controller = new EnergyController();
        try {
            controller.getAndPublishEnergyPrices(topicName);
        } catch (MySenderException e) {
            throw new RuntimeException(e);
        }
    }
}

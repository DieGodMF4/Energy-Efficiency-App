package marrero_ferrera_gcid_ulpgc.control;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws MyReceiverException {
        String topicNameWeather = (args.length > 0) ? args[0] : "prediction.Weather";
        String topicNameEnergy = (args.length > 1) ? args[1] : "energy.MarketPrices";
        String url = (args.length > 2) ? args[2] : "tcp://localhost:61616";
        String clientID = (args.length > 3) ? args[3] : "anonymous";
        String basePath = (args.length > 4) ? args[4] : "datalake" + File.separator + "eventstore" + File.separator;
        TopicSubscriber subscriberWeather = new TopicSubscriber(url, topicNameWeather, clientID, basePath);
        TopicSubscriber subscriberEnergy = new TopicSubscriber(url, topicNameEnergy, clientID, basePath);

        CountDownLatch latch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            try {
                subscriberWeather.receiveMessage();
            } catch (MyReceiverException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        executorService.submit(() -> {
            try {
                subscriberEnergy.receiveMessage();
            } catch (MyReceiverException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new MyReceiverException("Error waiting for threads", e);
        }
    }
}
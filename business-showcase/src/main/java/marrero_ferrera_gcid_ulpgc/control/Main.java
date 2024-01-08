package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import marrero_ferrera_gcid_ulpgc.view.ViewFirst;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> ViewFirst.launch(ViewFirst.class)).start();
        // Misma operación pero en la clase "ViewFinal" para mostrar los resultados tras las operaciones.
    }

    public static void updateUserValues(String url, String topicNameWeather, String topicNameEnergy, float powerChargeSolar,
                                        float powerChargeWind, float batteryCapacity, boolean recommendedHalfBattery) throws MyManagerException {
        JsonOperator operator = new JsonOperator(powerChargeSolar, powerChargeWind, batteryCapacity, recommendedHalfBattery);
        DataLakeFetcher weatherFetcher = new DataLakeFetcher(topicNameWeather, new Weather().getSs(), Instant.now(), operator);
        DataLakeFetcher energyFetcher = new DataLakeFetcher(topicNameEnergy, new EnergyPrice().getSs(), Instant.now(), operator);
        Subscriber weatherSubscriber = new TopicSubscriber(url, topicNameWeather, "WeatherConsumer", operator);
        Subscriber energySubscriber = new TopicSubscriber(url, topicNameEnergy, "EnergyConsumer", operator);

        BusinessController controllerWeather = new BusinessController(weatherFetcher, weatherSubscriber);
        BusinessController controllerEnergy = new BusinessController(energyFetcher, energySubscriber);
        CountDownLatch latch = new CountDownLatch(2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            try {
                controllerWeather.execute();
            } catch (MyManagerException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        executorService.submit(() -> {
            try {
                controllerEnergy.execute();
            } catch (MyManagerException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new MyManagerException("Error waiting for threads", e);
        }
    }
}

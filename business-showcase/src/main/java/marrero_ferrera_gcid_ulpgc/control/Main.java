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

    public static void updateUserValues(float latitude, float longitude, String url, String topicNameWeather, String topicNameEnergy, float powerChargeSolar,
                                        float powerChargeWind, float batteryCapacity, boolean recommendedHalfBattery) throws MyManagerException {
        Fetcher weatherFetcher = new DataLakeFetcher(topicNameWeather, new Weather().getSs(), Instant.now());
        Fetcher energyFetcher = new DataLakeFetcher(topicNameEnergy, new EnergyPrice().getSs(), Instant.now());
        Subscriber weatherSubscriber = new TopicSubscriber(topicNameWeather, "WeatherConsumer");
        Subscriber energySubscriber = new TopicSubscriber(topicNameEnergy, "EnergyConsumer");

        CountDownLatch latch = new CountDownLatch(2);

        // Crear ExecutorService para ejecutar hilos
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Ejecutar fetcher, operate y topicSubscriber en hilos separados
        executorService.submit(() -> {
            weatherFetcher.fetchFiles();
            // Operaciones adicionales si es necesario
            try {
                weatherSubscriber.receiveMessage();
            } catch (MyManagerException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        executorService.submit(() -> {
            energyFetcher.fetchFiles();
            // Operaciones adicionales si es necesario
            try {
                energySubscriber.receiveMessage();
            } catch (MyManagerException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        try {
            // Esperar a que ambos hilos terminen
            latch.await();
        } catch (InterruptedException e) {
            throw new MyManagerException("a", e);
        }
        // Ejecutar fetcher, operate y topicSubscriber
    }
}

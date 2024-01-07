package marrero_ferrera_gcid_ulpgc.control;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusinessController {
    private final Fetcher fetcher;
    private final Subscriber subscriber;

    public BusinessController(DataLakeFetcher fetcher, Subscriber subscriber) {
        this.fetcher = fetcher;
        this.subscriber = subscriber;
    }

    public void execute() throws MyManagerException {
        if (fetcher.fileExists()) {
            fetcher.fetchFiles();
        } else {
            System.out.println("No file found. ");
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                subscriber.receiveMessage();
            } catch (MyManagerException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.shutdown();
    }
}

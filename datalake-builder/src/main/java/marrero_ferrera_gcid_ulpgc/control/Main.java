package marrero_ferrera_gcid_ulpgc.control;

import java.io.File;

public class Main {
    public static void main(String[] args) throws MyReceiverException {
        String topicName = (args.length > 0) ? args[0] : "prediction.Weather";
        String url = (args.length > 1) ? args[1] : "tcp://localhost:61616";
        String clientID = (args.length > 2) ? args[2] : "anonymous";
        String basePath = (args.length > 3) ? args[3] : "datalake" + File.separator + "eventstore" + File.separator;
        TopicSubscriber subscriber = new TopicSubscriber(url, topicName, clientID, basePath);
        subscriber.receiveMessage();
    }
}
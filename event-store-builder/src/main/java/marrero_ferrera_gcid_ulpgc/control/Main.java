package marrero_ferrera_gcid_ulpgc.control;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws MyReceiverException {
        String topicName = (args.length > 0) ? args[0] : "prediction.Weather";
        String clientID = (args.length > 1) ? args[1] : "anonymous";
        String basePath = (args.length > 2) ? args[2] : "datalake/eventstore/prediction.Weather/";
        TopicSubscriber subscriber = new TopicSubscriber(topicName, clientID);
        ArrayList<String> messages = subscriber.receiveMessage();

        FileEventStoreBuilder builder = new FileEventStoreBuilder(basePath);
        builder.storeMessages(messages);
    }
}
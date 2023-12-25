package marrero_ferrera_gcid_ulpgc.control;


public class Main {
    public static void main(String[] args) throws MyReceiverException {
        String topicName = (args.length > 0) ? args[0] : "prediction.Weather";
        String clientID = (args.length > 1) ? args[1] : "anonymous";
        String basePath = (args.length > 2) ? args[2] : "datalake/eventstore/";
        TopicSubscriber subscriber = new TopicSubscriber(topicName, clientID, basePath);
        subscriber.receiveMessage();
    }
}
package marrero_ferrera_gcid_ulpgc.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public final class TopicSubscriber implements Subscriber {
    private final String url;
    private final String topicName;
    private final String clientID;
    private final JsonOperator operator;
    private final ArrayList<String> results;


    public TopicSubscriber(String url, String topicName, String clientID, JsonOperator operator) {
        this.url = url;
        this.topicName = topicName;
        this.clientID = clientID;
        this.operator = operator;
        this.results = new ArrayList<>();
    }

    @Override
    public void receiveMessage() throws MyManagerException {
        try {
            MessageConsumer consumer = buildConnectionAndTopic();
            CountDownLatch latch = new CountDownLatch(1);
            consumer.setMessageListener(message -> {
                try {
                    operator.operateAsSubscriber(((TextMessage) message).getText());
                } catch (JMSException | MyManagerException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
            System.out.println("Running...");

        } catch (JMSException e) {
            throw new MyManagerException("Error receiving message from ActiveMQ", e);
        }
    }

    private MessageConsumer buildConnectionAndTopic() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(clientID);
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);

        return session.createDurableSubscriber(topic, clientID);
    }
    public ArrayList<String> getResults() {
        return results;
    }

}


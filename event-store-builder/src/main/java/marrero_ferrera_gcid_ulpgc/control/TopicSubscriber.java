package marrero_ferrera_gcid_ulpgc.control;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public final class TopicSubscriber implements Subscriber {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final String topicName;
    private final String clientID;

    public TopicSubscriber(String topicName, String clientID) {
        this.topicName = topicName;
        this.clientID = clientID;
    }

    @Override
    public ArrayList<String> receiveMessage() throws MyReceiverException {
        ArrayList<String> answers = new ArrayList<>();
        try {
            MessageConsumer consumer = buildConnectionAndTopic();

            CountDownLatch latch = new CountDownLatch(1);
            consumer.setMessageListener(message -> {
                try {
                    answers.add(((TextMessage) message).getText());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
            System.out.println("running");
            return answers;

        } catch (JMSException e) {
            throw new MyReceiverException("Error receiving message from ActiveMQ", e);
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

}


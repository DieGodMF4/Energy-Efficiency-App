package marrero_ferrera_gcid_ulpgc.control;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public record JMSWeatherStore(String topicName) implements WeatherStore {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    @Override
    public void insertWeather(String jsonDataContainer) {
        try {
            Connection connection = buildConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);

            TextMessage message = session.createTextMessage(jsonDataContainer);
            producer.send(message);
            connection.close();
        } catch (JMSException e) {
            e.getCause();
        }
    }

    private static Connection buildConnection () throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }
}

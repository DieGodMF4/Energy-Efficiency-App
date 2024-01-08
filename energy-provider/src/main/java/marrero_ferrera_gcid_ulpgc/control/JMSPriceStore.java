package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.lang.reflect.Type;
import java.time.Instant;

public record JMSPriceStore(String url, String topicName) implements PriceStore {

    @Override
    public void insertPrice(EnergyPrice energyPrice) {
        try {
            Connection connection = buildConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(toJson(energyPrice));
            producer.send(message);
            connection.close();
        } catch (JMSException e) {
            e.getCause();
        }
    }

    private String toJson(EnergyPrice energyPrice) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .create();
        return gson.toJson(energyPrice);
    }

    private Connection buildConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    static class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(instant.toString());
        }
    }
}

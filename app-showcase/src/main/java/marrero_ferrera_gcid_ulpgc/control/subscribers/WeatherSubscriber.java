package marrero_ferrera_gcid_ulpgc.control.subscribers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import marrero_ferrera_gcid_ulpgc.control.InstantSerializer;
import marrero_ferrera_gcid_ulpgc.control.handlers.WeatherHandler;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;
import java.time.Instant;

public class WeatherSubscriber {
    private final Session session;
    private final String topic;
    private final WeatherHandler weatherHandler;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .create();

    public WeatherSubscriber(Session session, String topic, WeatherHandler weatherHandler) {
        this.session = session;
        this.topic = topic;
        this.weatherHandler = weatherHandler;
    }

    public void start() {
        try {
            TopicSubscriber durableSubscriber = session.createDurableSubscriber(new ActiveMQTopic(topic), "client-id " + topic);
            durableSubscriber.setMessageListener(m -> {
                try {
                    weatherHandler.handle(gson.fromJson(((TextMessage) m).getText(), Weather.class));
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

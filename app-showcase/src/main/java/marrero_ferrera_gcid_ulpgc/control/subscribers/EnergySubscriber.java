package marrero_ferrera_gcid_ulpgc.control.subscribers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import marrero_ferrera_gcid_ulpgc.control.InstantSerializer;
import marrero_ferrera_gcid_ulpgc.control.handlers.EnergyHandler;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;
import java.time.Instant;

public class EnergySubscriber {
    private final Session session;
    private final String topic;
    private final EnergyHandler energyHandler;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .create();

    public EnergySubscriber(Session session, String topicNameEnergy, EnergyHandler energyHandler) {
        this.session = session;
        this.topic = topicNameEnergy;
        this.energyHandler = energyHandler;
    }

    public void start() {
        try {
            TopicSubscriber durableSubscriber = session.createDurableSubscriber(new ActiveMQTopic(topic), "client-id " + topic);
            durableSubscriber.setMessageListener(message -> {
                try {
                    energyHandler.handle(gson.fromJson(((TextMessage) message).getText(), EnergyPrice.class));
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

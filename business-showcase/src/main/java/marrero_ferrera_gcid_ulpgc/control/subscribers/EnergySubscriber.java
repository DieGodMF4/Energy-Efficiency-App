package marrero_ferrera_gcid_ulpgc.control.subscribers;

import com.google.gson.Gson;
import marrero_ferrera_gcid_ulpgc.control.handlers.EnergyHandler;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;

public class EnergySubscriber {
    private final Session session;
    private final String topic;
    private final EnergyHandler energyHandler;

    public EnergySubscriber(Session session, String topicNameEnergy, EnergyHandler energyHandler) {
        this.session = session;
        this.topic = topicNameEnergy;
        this.energyHandler = energyHandler;
    }

    public void start() {
        try {
            TopicSubscriber durableSubscriber = session.createDurableSubscriber(new ActiveMQTopic(topic), "client-id " + topic);
            durableSubscriber.setMessageListener(m -> {
                try {
                    energyHandler.handle(new Gson().fromJson(((TextMessage) m).getText(), EnergyPrice.class));
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

}

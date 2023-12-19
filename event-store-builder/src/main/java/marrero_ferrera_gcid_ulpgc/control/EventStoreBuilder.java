package marrero_ferrera_gcid_ulpgc.control;

import java.util.ArrayList;

public interface EventStoreBuilder {
    void storeMessages(ArrayList<String> message) throws MyReceiverException;
}

package marrero_ferrera_gcid_ulpgc.control;

import java.util.ArrayList;

public interface Subscriber {
    ArrayList<String> receiveMessage() throws MyReceiverException;
}

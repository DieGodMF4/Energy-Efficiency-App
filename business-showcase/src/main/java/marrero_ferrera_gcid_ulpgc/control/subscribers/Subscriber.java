package marrero_ferrera_gcid_ulpgc.control.subscribers;

import marrero_ferrera_gcid_ulpgc.control.MyManagerException;

public interface Subscriber {
    void receiveMessage() throws MyManagerException;
}

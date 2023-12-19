import marrero_ferrera_gcid_ulpgc.control.FileEventStoreBuilder;
import marrero_ferrera_gcid_ulpgc.control.MyReceiverException;

import java.util.ArrayList;

public class JsonTest {

    public static void main(String[] args) throws MyReceiverException {
        ArrayList<String> jsonMessages = new ArrayList<>();
        jsonMessages.add("{\"ss\":\"WeatherProvider\",\"ts\":1702039743,\"data\":\"SampleData4\"}");
        jsonMessages.add("{\"ss\":\"WeatherProvider\",\"ts\":1702039744,\"data\":\"SampleData5\"}");
        jsonMessages.add("{\"ss\":\"WeatherProvider\",\"ts\":1702039744,\"data\":\"SampleData55\"}");
        FileEventStoreBuilder storeBuilder = new FileEventStoreBuilder("eventstore/prediction.Weather/");
        storeBuilder.storeMessages(jsonMessages);
    }
}

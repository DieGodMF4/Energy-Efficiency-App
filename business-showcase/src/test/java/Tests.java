import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import marrero_ferrera_gcid_ulpgc.control.DataLakeFetcher;
import marrero_ferrera_gcid_ulpgc.control.Fetcher;
import marrero_ferrera_gcid_ulpgc.control.JsonOperator;
import marrero_ferrera_gcid_ulpgc.control.MyManagerException;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import marrero_ferrera_gcid_ulpgc.model.Model;
import marrero_ferrera_gcid_ulpgc.view.ViewFinal;

import java.time.Instant;
import java.util.ArrayList;

public class Tests {
    public static void main(String[] args) throws MyManagerException {
        Instant date = Instant.parse("2024-01-08T23:00:00Z");
        JsonOperator operator = new JsonOperator(10.0f,5.0f,100.0f,false);
        DataLakeFetcher fetcher = new DataLakeFetcher("prediction.Weather", new Weather().getSs(), date, operator);
        fetcher.fetchFiles();
        //ArrayList<String> strings = fetcher.getResults();
        //System.out.println(strings.size());
        //for (String string : strings) System.out.println(string);
    }
}

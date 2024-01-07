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
        Instant date = Instant.parse("2024-01-05T23:00:00Z");
        JsonOperator operator = new JsonOperator(0.0f,0.0f,0.0f,true);
        DataLakeFetcher fetcher = new DataLakeFetcher("energy.MarketPrices", new EnergyPrice().getSs(), date, operator);
        fetcher.fetchFiles();
        ArrayList<String> strings = fetcher.getResults();
        System.out.println(strings.size());
        //for (String string : strings) System.out.println(string);
    }
}

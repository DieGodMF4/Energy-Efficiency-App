import marrero_ferrera_gcid_ulpgc.control.DataLakeFetcher;
import marrero_ferrera_gcid_ulpgc.control.Fetcher;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;

public class Tests {
    public static void main(String[] args) {
        String date = String.valueOf(Instant.parse("2024-01-05T23:00:00Z"));
        Fetcher fetcher = new DataLakeFetcher("energy.MarketPrices", new EnergyPrice().getSs(), date);
        fetcher.fetchFilesJson();
    }
}

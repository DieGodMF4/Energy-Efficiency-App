package marrero_ferrera_gcid_ulpgc.model;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;

import java.time.Instant;

public class Model {
    private Instant predTimeEnergy;
    private final String ssEnergy;
    private final String ssWeather;
    private String weatherType;
    private float wind;
    private int clouds;
    private float price;
    private EnergyPrice.Slot slot;


    public Model(Instant predTimeEnergy, String weatherType, float wind, int clouds, float price, EnergyPrice.Slot slot) {
        this.predTimeEnergy = predTimeEnergy;
        this.ssEnergy = new EnergyPrice().getSs();
        this.ssWeather = new Weather().getSs();
        this.weatherType = weatherType;
        this.wind = wind;
        this.clouds = clouds;
        this.price = price;
        this.slot = slot;
    }
}

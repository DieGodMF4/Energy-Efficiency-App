package marrero_ferrera_gcid_ulpgc.model;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;

import java.time.Instant;

public class Model {
    private Instant predictionTime;
    private final String ssEnergy; // Probably won't be used
    private final String ssWeather; // Probably won't be used
    private String weatherType;
    private float windEfficiency;
    private int clouds;
    private float price;
    private EnergyPrice.Slot slot;


    public Model(Instant predictionTime, String weatherType, float windEfficiency, int clouds, float price, EnergyPrice.Slot slot) {
        this.predictionTime = predictionTime;
        this.ssEnergy = new EnergyPrice().getSs();
        this.ssWeather = new Weather().getSs();
        this.weatherType = weatherType;
        this.windEfficiency = windEfficiency;
        this.clouds = clouds;
        this.price = price;
        this.slot = slot;
    }
}

package marrero_ferrera_gcid_ulpgc.model;

import java.time.Instant;

public class Weather {
    private final Instant ts;
    private final String ss;
    private Instant predictionTime;
    private String weatherType;
    private int clouds;
    private float temperature;
    private float rain;
    private float humidity;
    private float wind;
    private Location location;

    @Override
    public String toString() {
        return "Weather{" +
                "ts=" + ts +
                ", ss='" + ss + '\'' +
                ", predictionTime=" + predictionTime +
                ", weatherType='" + weatherType + '\'' +
                ", clouds=" + clouds +
                ", temperature=" + temperature +
                ", rain=" + rain +
                ", humidity=" + humidity +
                ", wind=" + wind +
                ", location=" + location +
                '}';
    }

    public Weather(Instant predictionTime, String weatherType, int clouds, float temperature, float humidity, Location location, float rain, float wind) {
        this.predictionTime = predictionTime;
        this.weatherType = weatherType;
        this.ss = "WeatherProvider";
        this.clouds = clouds;
        this.temperature = temperature;
        this.humidity = humidity;
        this.location = location;
        this.ts = Instant.now();
        this.rain = rain;
        this.wind = wind;
    }

    public Weather() {
        this.ss = "WeatherProvider";
        this.ts = Instant.now();
    }

    public Weather setPredictionTime(Instant predictionTime) {
        this.predictionTime = predictionTime;
        return this;
    }

    public Weather setWeatherType(String weatherType) {
        this.weatherType = weatherType;
        return this;
    }

    public Weather setClouds(int clouds) {
        this.clouds = clouds;
        return this;
    }

    public Weather setTemperature(float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Weather setRain(float rain) {
        this.rain = rain;
        return this;
    }

    public Weather setHumidity(float humidity) {
        this.humidity = humidity;
        return this;
    }

    public Weather setWind(float wind) {
        this.wind = wind;
        return this;
    }

    public Weather setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getSs() {
        return ss;
    }

    public Instant getTs() {
        return ts;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public int getClouds() {
        return clouds;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getRain() {
        return rain;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWind() {
        return wind;
    }

    public Location getLocation() {
        return location;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public record Location(float latitude, float longitude, String name) {
    }
}

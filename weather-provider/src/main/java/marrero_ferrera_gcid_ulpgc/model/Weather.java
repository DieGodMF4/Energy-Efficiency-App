package marrero_ferrera_gcid_ulpgc.model;

import java.time.Instant;

public class Weather {
    private final Instant ts;
    private final String weatherType;
    private final int clouds;
    private final float temperature;
    private final float rain;
    private final float humidity;
    private final float wind;
    private final Location location;

    public Weather(String weatherType, int clouds, float temperature, float humidity, Location location, Instant ts, float rain, float wind) {
        this.weatherType = weatherType;
        this.clouds = clouds;
        this.temperature = temperature;
        this.humidity = humidity;
        this.location = location;
        this.ts = ts;
        this.rain = rain;
        this.wind = wind;
    }

    public int getClouds() {
        return clouds;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public Location getLocation() {
        return location;
    }

    public String getTsToString() {
        return ts.toString();
    }

    public String getWeatherType() {
        return weatherType;
    }

    public float getRain() {
        return rain;
    }

    public float getWind() {
        return wind;
    }

    public String getSs() {
        return "WeatherProvider";
    }

    public long getTs() {
        return ts.getEpochSecond();
    }
}

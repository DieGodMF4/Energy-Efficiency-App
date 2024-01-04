package marrero_ferrera_gcid_ulpgc.control.schemas;

import java.time.Instant;

public class Weather {
    private final Instant ts;
    private final Instant predictionTime;
    private final String weatherType;
    private final int clouds;
    private final float temperature;
    private final float rain;
    private final float humidity;
    private final float wind;
    private final Location location;

    public Weather(Instant predictionTime, String weatherType, int clouds, float temperature, float humidity, Location location, float rain, float wind) {
        this.predictionTime = predictionTime;
        this.weatherType = weatherType;
        this.clouds = clouds;
        this.temperature = temperature;
        this.humidity = humidity;
        this.location = location;
        this.ts = Instant.now();
        this.rain = rain;
        this.wind = wind;
    }

    public String getSs() {
        return "WeatherProvider";
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

    public static class Location {
        private final float latitude;
        private final float longitude;
        private final String name;

        public Location(float latitude, float longitude, String name) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
        }

        public float getLatitude() {
            return latitude;
        }

        public float getLongitude() {
            return longitude;
        }
    }
}

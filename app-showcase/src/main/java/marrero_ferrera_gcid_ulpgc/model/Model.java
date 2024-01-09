package marrero_ferrera_gcid_ulpgc.model;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;

import java.time.Instant;
import java.util.ArrayList;

public class Model {
    ArrayList<Item> weatherItems;
    ArrayList<Item> energyItems;
    ArrayList<Item> finalItems;
    float powerChargeSolar;
    float powerChargeWind;
    float batteryCapacity;
    boolean recommendedHalfBattery;

    public Model(){
        this.weatherItems = new ArrayList<>();
        this.energyItems = new ArrayList<>();
        this.finalItems = new ArrayList<>();
    }

    public ArrayList<Item> getWeatherItems() {
        return weatherItems;
    }

    public ArrayList<Item> getEnergyItems() {
        return energyItems;
    }

    public ArrayList<Item> getFinalItems() {
        return finalItems;
    }

    public void addEnergyItem(Item item) {
        energyItems.add(item);
    }
    public void addWeatherItem(Item item) {
        weatherItems.add(item);
    }
    public void addFinalItem(Item item) {
        finalItems.add(item);
    }

    public void setRenewableFields(float powerChargeSolar, float powerChargeWind, float batteryCapacity,
                                   boolean recommendedHalfBattery) {
        this.powerChargeSolar = powerChargeSolar;
        this.powerChargeWind = powerChargeWind;
        this.batteryCapacity = batteryCapacity;
        this.recommendedHalfBattery = recommendedHalfBattery;
    }

    public float getPowerChargeSolar() {
        return powerChargeSolar;
    }

    public float getPowerChargeWind() {
        return powerChargeWind;
    }

    public boolean isRecommendedHalfBattery() {
        return recommendedHalfBattery;
    }

    public float getBatteryCapacity() {
        return batteryCapacity;
    }

    public static class Item {
        private Instant predictionTime;
        private String weatherType;
        private float windGained;
        private float solarGained;
        private float batteryGained;
        private float price;
        private EnergyPrice.Slot slot;

        public Item(Instant predictionTime, String weatherType, float windGained, float solarGained, float batteryGained, float price, EnergyPrice.Slot slot) {
            this.predictionTime = predictionTime;
            this.weatherType = weatherType;
            this.windGained = windGained;
            this.solarGained = solarGained;
            this.batteryGained = batteryGained;
            this.price = price;
            this.slot = slot;
        }

        public Item() {
        }

        public void setPredictionTime(Instant predictionTime) {
            this.predictionTime = predictionTime;
        }

        public void setWeatherType(String weatherType) {
            this.weatherType = weatherType;
        }

        public void setWindGained(float windGained) {
            this.windGained = windGained;
        }

        public void setSolarGained(float solarGained) {
            this.solarGained = solarGained;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public void setSlot(EnergyPrice.Slot slot) {
            this.slot = slot;
        }

        public Instant getPredictionTime() {
            return predictionTime;
        }

        public String getWeatherType() {
            return weatherType;
        }

        public float getWindGained() {
            return windGained;
        }

        public float getPrice() {
            return price;
        }

        public EnergyPrice.Slot getSlot() {
            return slot;
        }

        public float getSolarGained() {
            return solarGained;
        }

        public float getBatteryGained() {
            return batteryGained;
        }

        public void setBatteryGained(float batteryGained) {
            this.batteryGained = batteryGained;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "predictionTime=" + predictionTime +
                    ", weatherType='" + weatherType + '\'' +
                    ", windGained=" + windGained +
                    ", solarGained=" + solarGained +
                    ", batteryGained=" + batteryGained +
                    ", price=" + price +
                    ", slot=" + slot +
                    '}';
        }
    }
}

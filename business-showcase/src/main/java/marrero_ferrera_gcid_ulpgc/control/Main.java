package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.view.View;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> View.launch(View.class)).start();
    }


    public static void updateUserValues(String url, String topicNameWeather, String topicNameEnergy,
                                        float powerChargeSolar, float powerChargeWind, boolean recommendedHalfBattery) {
        System.out.println("Operaciones en Main:");
        System.out.println("URL: " + url);
        System.out.println("Topic Name Weather: " + topicNameWeather);
        System.out.println("Topic Name Energy: " + topicNameEnergy);
        System.out.println("Power Charge Solar: " + powerChargeSolar);
        System.out.println("Power Charge Wind: " + powerChargeWind);
        System.out.println("Recommended Half Battery: " + recommendedHalfBattery);
    }
}

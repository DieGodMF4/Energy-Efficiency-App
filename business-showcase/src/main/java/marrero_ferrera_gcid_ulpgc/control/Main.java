package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.view.ViewFirst;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> ViewFirst.launch(ViewFirst.class)).start();
        // Misma operación pero en la clase "ViewFinal" para mostrar los resultados tras las operaciones.
    }


    public static void updateUserValues(String url, String topicNameWeather, String topicNameEnergy, float powerChargeSolar,
                                        float powerChargeWind, float batteryCapacity, boolean recommendedHalfBattery) {

        // Hacer operaciones con las variables en otra clase "Controller"
    }
}

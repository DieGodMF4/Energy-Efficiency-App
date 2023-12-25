package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.EnergyPrice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MySenderException {
        ElectricEnergySupplier supplier = new ElectricEnergySupplier();
        ArrayList<EnergyPrice> prices = supplier.getPriceForToday();
        System.out.println(prices.size());
        List<EnergyPrice> sortedPrices = prices.stream()
                .sorted(Comparator.comparingDouble(EnergyPrice::getPrice)).toList();
        for (EnergyPrice price : sortedPrices) {
            System.out.println(price.getPrice() + price.getState().toString());
        }
    }
}

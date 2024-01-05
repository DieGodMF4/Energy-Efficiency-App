package marrero_ferrera_gcid_ulpgc.model;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;

import java.time.Instant;

public class Model {
    //TODO clase para el cálculo de la recomendación en base a los eventos.
    private Instant predictionTime;
    private String weatherType;
    private float wind;
    private int clouds;
    private float price;
    private EnergyPrice.Slot slot;

}

package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildModelFinal {
    private List<Model> finalModels;

    public BuildModelFinal() {
        this.finalModels = new ArrayList<>();
    }

    public void buildFinalModels(ArrayList<Model> partialModels) {
        // Usar un Map para agrupar los modelos por predictionTime
        Map<Instant, Model> modelMap = new HashMap<>();

        for (Model partialModel : partialModels) {
            Instant predictionTime = partialModel.getPredictionTime();

            // Obtener el modelo existente en el map o crear uno nuevo si no existe
            Model aggregatedModel = modelMap.getOrDefault(predictionTime, new Model());

            // Actualizar los atributos del modelo existente con los valores no nulos del partialModel
            if (true) { // partialModel.getPrice() != null
                aggregatedModel.setPrice(partialModel.getPrice());
            }
            if (partialModel.getWeatherType() != null) {
                aggregatedModel.setWeatherType(partialModel.getWeatherType());
            }
            // Agregar más atributos según sea necesario

            // Actualizar el modelo en el map
            modelMap.put(predictionTime, aggregatedModel);
        }

        // Agregar los modelos agrupados a la lista finalModels
        finalModels.addAll(modelMap.values());
    }

    public List<Model> getFinalModels() {
        return finalModels;
    }
}

package marrero_ferrera_gcid_ulpgc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewFinal {

    @FXML
    private Label parameter1Label;

    @FXML
    private Label parameter2Label;

    // Agrega más anotaciones @FXML para otras etiquetas

    // Este método se llamará para inicializar los datos en la vista
    public void initializeData(String parameter1, String parameter2) {
        parameter1Label.setText(parameter1);
        parameter2Label.setText(parameter2);
        // Establece otros valores para otras etiquetas según sea necesario
    }

    @FXML
    private void handleClose() {
        // Lógica para cerrar la vista o realizar otras acciones al hacer clic en el botón "Close"
    }
}
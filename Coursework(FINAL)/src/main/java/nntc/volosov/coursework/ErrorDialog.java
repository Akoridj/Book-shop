package nntc.volosov.coursework;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class ErrorDialog {

    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }
}

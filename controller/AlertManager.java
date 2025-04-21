package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertManager {
    
    // Function to show warning alerts
    static void showWarning(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            return true;
        }
        
        return false;

    }

    static void showInformation(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}

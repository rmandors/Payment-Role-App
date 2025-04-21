package controller;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import model.Employee;

public class GUIObservers {

    // Initializes idField observer, checks if id is valid
    static void idObserver(Controller controller, ListView<Employee> listView) {

        controller.idField.focusedProperty().addListener((_,_,newValue) -> {
            if (!newValue && !(listView.getSelectionModel().getSelectedItem() == null)) {
                String input = controller.idField.getText().trim();

                if (!(input.matches("[0-9]+") || input.matches(""))) {
                    AlertManager.showWarning("Entrada invalida!","El campo de ID solo acepta valores númericos.");
                    controller.idField.setText("");
                }
                else if (Controller.idSet.contains(Integer.parseInt(input)) && !(listView.getSelectionModel().getSelectedItem().getId() == Integer.parseInt(input))) {
                    AlertManager.showWarning("Entrada invalida!","El ID escogido ya está en uso.");
                    controller.idField.setText("");
                }
            }
        });
        
    }

/*     static void formatObserver(Controller controller, ComboBox<String> comboBox){
        comboBox.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> _, String _, String newValue) -> {
                if (newValue == "Consola (Unitario)") {
                    controller.exportData.setText("Buscar");
                }
                else {
                    controller.exportData.setText("Exportar");
                }
        });
    } */

}

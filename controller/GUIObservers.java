package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;
import model.Employee;

public class GUIObservers {

    public static void validateObservers(Controller controller, ListView<Employee> listView) {

        controller.idField.focusedProperty().addListener((_,_,newValue) -> {
            if (!newValue /* && !Controller.regListView.getSelectionModel().getSelectedItem().equals(null) */) {
                String input = controller.idField.getText().trim();

                if (!(input.matches("[0-9]+") || input.matches(""))) {
                    Controller.showWarning("Entrada invalida!","El campo de ID solo acepta valores númericos.");
                    controller.idField.setText("");
                }
                else if (Controller.idSet.contains(Integer.parseInt(input)) && !(listView.getSelectionModel().getSelectedItem().getId() == Integer.parseInt(input))) {
                    Controller.showWarning("Entrada invalida!","El ID escogido ya está en uso.");
                    controller.idField.setText("");
                }
            }
        });
        
    }

}

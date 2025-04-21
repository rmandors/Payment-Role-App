package controller;

import model.Employee;
import model.Manager;

import java.io.File;

import java.time.LocalDate;

import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

@SuppressWarnings("deprecation")
public class Controller {

    // Declare combobox options
    private int newRegCounter = 1;
    private int CSVExportsCounter = 2;
    private int XMLExportsCounter = 1;
    private int JSONExportsCounter = 1;
    private int finalReportExportsCounter = 1;
    
    private static String[] typeOptions = {"Employee","Manager"};     
    private static String[] formatOptions = {"CSV","XML","JSON","Archivo de texto","Consola (Completo)", "Consola (Unitario)"};       
    private static String[] orderOptions = {"Lastname","Salary","Hire Date"}; 
 
    // Declare set of ids
    static ObservableSet<Integer> idSet = FXCollections.observableSet();

    // Declare observables array lists
    static ObservableList<Employee> employees = FXCollections.observableArrayList();
    private static ObservableList<String> typeItems = FXCollections.observableArrayList(typeOptions);
    private static ObservableList<String> formatItems = FXCollections.observableArrayList(formatOptions);
    private static ObservableList<String> orderItems = FXCollections.observableArrayList(orderOptions);

    // Declare fxml buttons
    @FXML Button deleteReg;
    @FXML Button exportData;
    @FXML Button importData;
    @FXML Button newReg;
    @FXML Button updateReg;

    // Declare fxml comboboxes
    @FXML ComboBox<String> formatCombobox;
    @FXML ComboBox<String> orderComboBox;
    @FXML ComboBox<String> typeComboBox;

    // Declare fxml date picker
    @FXML DatePicker datePicker;

    // Declare fxml label
    @FXML Label fullNameField;

    // Declare fxml list view
    @FXML ListView<Employee> regListView;

    // Declare fxml text fields
    @FXML TextField idField;
    @FXML TextField comissionField;
    @FXML TextField lastnameField;
    @FXML TextField nameField;
    @FXML TextField salaryField;
    @FXML TextField titleField;

    // Funtion to delete register
    @FXML
    void deleteReg(ActionEvent event) {

        if (regListView.getSelectionModel().getSelectedItem() == null) return;
        boolean confirm = AlertManager.showConfirmation("Eliminar Registro",
                                                        "¿Estás seguro que deseas eliminar este registro?");

        if(confirm){
            // Delete the selected employee
            Employee selected = regListView.getSelectionModel().getSelectedItem();
            if (selected != null){
                fullNameField.setText("Nombre y Apellido");
                idField.clear();
                nameField.clear();
                lastnameField.clear();
                datePicker.setValue(null);
                salaryField.clear();
                titleField.clear();
                comissionField.clear();
                employees.remove(selected);                
            }
        } 
    }

    // Function to update register
    @FXML
    void updateReg(ActionEvent event){
        Employee selected = regListView.getSelectionModel().getSelectedItem();
        if(selected != null){
            // Logic if class is not changed
            if (selected.getClass().getName().equals("model."+ typeComboBox.getValue())) {
                try{
                    fullNameField.setText(nameField.getText()+" "+lastnameField.getText());
                    selected.setId(Integer.parseInt(idField.getText()));
                    selected.setName(nameField.getText());
                    selected.setLastname(lastnameField.getText());
                    selected.setSalary(Float.parseFloat(salaryField.getText()));
    
                    LocalDate localDate = datePicker.getValue();
                    if(localDate != null){
                        Date hireDate = new Date(localDate.getYear(), 
                                             localDate.getMonthValue() - 1, 
                                             localDate.getDayOfMonth());
                        selected.setHireDate(hireDate);
                    }
    
                    if(typeComboBox.getValue().equals("Manager")){
                        comissionField.setText(String.valueOf(((Manager)selected).getCommission()));
                        titleField.setEditable(true);
                        ((Manager)selected).setEducationLevel(titleField.getText());
                    }
                    else{
                        titleField.setEditable(false);
                        titleField.setText("none");
                        comissionField.setText("0");
                    }   

                    // Reorder after updating
                    if(orderComboBox.getValue().equals("Lastname")) 
                        SortManager.sortByLastname(); 
                    else if(orderComboBox.getValue().equals("Salary")) 
                        SortManager.sortBySalary();
                    else if(orderComboBox.getValue().equals("Hire Date"))
                        SortManager.sortByHireDate();
                    AlertManager.showInformation("Información Actualizada!",
                                                 "Información actualizada correctamente.");
                    regListView.refresh();
                }
                catch(IllegalArgumentException e){
                    AlertManager.showWarning("Entrada invalida!", e.getMessage());
                }

            }
            // Logic if class changed to Manager
            else if (typeComboBox.getValue().equals("Manager")) {
                try {
                    Manager updatedReg = new Manager(selected.getId(), selected.getName(), selected.getLastname(), 
                                                     selected.getDate(), selected.getSalary(), titleField.getText());
                    int index = employees.indexOf(selected);
                    employees.remove(selected);
                    employees.add(index, updatedReg);
                    regListView.refresh();
                    regListView.getSelectionModel().select(index);
                } catch (IllegalArgumentException e) {
                    AlertManager.showWarning("Entrada invalida!", e.getMessage());
                }
            }
            // Logic if class changed to employee
            else if (typeComboBox.getValue().equals("Employee")) {
                try {
                    Employee updatedReg = new Employee(selected.getId(), selected.getName(), selected.getLastname(),
                                                       selected.getDate(), selected.getSalary());
                    int index = employees.indexOf(selected);
                    employees.remove(selected);
                    employees.add(index, updatedReg);
                    regListView.refresh();
                    regListView.getSelectionModel().select(index);
                } catch (Exception e) {
                    AlertManager.showWarning("Entrada invalida!", e.getMessage());
                }
            }
            
            
        }
    }

    // Function to create a new registry
    @FXML
    void newReg(ActionEvent event){
        // Logic for assigning lowest id
        if (idSet.contains(newRegCounter)) {
            newRegCounter = 1;
            int counter = 0;
            while (idSet.contains(newRegCounter) && (counter <= 10000)) {
                counter++;
                newRegCounter++;
            }
            if (counter > 9999) {
                AlertManager.showWarning("Error Fatal!", 
                                         "Número máximo de IDs alcanzados, elimina los registros previos antes de crear uno nuevo");
                return;                
            }
        }
        // Create new employee
        Employee newEmployee = new Employee(newRegCounter,"Unknown","Unknown",new Date(2025,3,21), 800);
        employees.add(newEmployee);
        regListView.selectionModelProperty().get().select(newEmployee);
    }

    // Function to manage export logic
    @FXML
    void exportData(ActionEvent event){
        String selectedFormat = formatCombobox.getValue();
        String filename = "employeesData";

        if(selectedFormat.equals("CSV")){
            filename += CSVExportsCounter;
            ExportManager.exportCSV(filename + ".csv");
            CSVExportsCounter++;
        }
        else if(selectedFormat.equals("XML")){
            if(XMLExportsCounter > 1) {
                filename += XMLExportsCounter;
            }
            ExportManager.exportXML(filename + ".xml");
            XMLExportsCounter++;
        }
        else if(selectedFormat.equals("JSON")){
            if(JSONExportsCounter > 1) {
                filename += JSONExportsCounter;
            }
            ExportManager.exportJSON(filename + ".json");
            JSONExportsCounter++;
        }
        else if(selectedFormat.equals("Archivo de texto")) {
            if(finalReportExportsCounter > 1) {
                filename += finalReportExportsCounter;
            }
            ExportManager.exportFinalReport(filename + ".txt");
            finalReportExportsCounter++;
        }
        else if(selectedFormat.equals("Consola (Completo)")){
            ConsoleManager.exportToConsole();
        }
        else if(selectedFormat.equals("Consola (Unitario)")){
            ConsoleManager.searchForUserId();
        }
    }

    // Function to import csv through import button
    @FXML
    void importData(ActionEvent event){ 
        
        // Create file chooser
        FileChooser csvChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Archivos CSV", "*.csv");
        Stage mainStage = (Stage) fullNameField.getScene().getWindow();
        
        csvChooser.setTitle("Importar CSV");
        csvChooser.getExtensionFilters().add(csvFilter);
        csvChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectionFile = csvChooser.showOpenDialog(mainStage); // Get file

        if (selectionFile != null) {
            // Clear registries and import file
            Employee selected = regListView.getSelectionModel().getSelectedItem();
            if(selected != null){
                fullNameField.setText("Nombre y Apellido");
                idField.clear();
                nameField.clear();
                lastnameField.clear();
                datePicker.setValue(null);
                salaryField.clear();
                titleField.clear();
                comissionField.clear();
            }
            employees.clear();
            ExportManager.importCSV(selectionFile);
            orderComboBox.setValue("Default");
        }
        
    }

    // Initialize program
    public void initialize(){

        // Listener to keep idSet updated
        employees.addListener((ListChangeListener<Employee>) change -> {
            while (change.next()) {
                if(change.wasAdded()) {
                    for (Employee employee : change.getAddedSubList()) {
                        idSet.add(employee.getId());
                        employee.idProperty().addListener((_,oldValue,newValue) -> {
                            idSet.remove(oldValue.intValue());
                            idSet.add(newValue.intValue());
                        });
                        
                    }
                }
                else if (change.wasRemoved()) {
                    for (Employee employee : change.getRemoved()) {
                        idSet.remove(employee.getId());
                    }
                }
            }
        } );

        // Init combobox and listener to enable/disble fields
        typeComboBox.getItems().addAll(typeItems);
        typeComboBox.setValue("Employee");

        typeComboBox.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> _, String _, String newValue) -> {
                if (newValue.equals("Employee")) {
                    titleField.setEditable(false);
                    titleField.setDisable(true);
                    comissionField.setDisable(true);
                    titleField.setText("none");
                    comissionField.setText("0");
                } 
                else if (newValue.equals("Manager")) {
                    titleField.setDisable(false);
                    comissionField.setDisable(false);
                    titleField.setEditable(true);
                    titleField.clear();
                }
            }
        );

        // Init comboboxes
        formatCombobox.getItems().addAll(formatItems);
        formatCombobox.setValue("CSV");

        orderComboBox.getItems().addAll(orderItems);
        orderComboBox.setValue("Default");

        // Listener to sort registers
        orderComboBox.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> _, String _, String newValue) -> {
                if(newValue.equals("Lastname")) 
                    SortManager.sortByLastname(); 
                else if(newValue.equals("Salary")) 
                    SortManager.sortBySalary();
                else if(newValue.equals("Hire Date"))
                    SortManager.sortByHireDate();
            }
        );
 
        if(comissionField != null){
            comissionField.setEditable(false);
        }

        // Initial import
        ExportManager.importCSV("employeesData.csv");
        regListView.setItems(employees);

        // Init id observer
        GUIObservers.idObserver(this, regListView);

        // Disable all fields for start
        typeComboBox.setDisable(true);
        idField.setDisable(true);
        nameField.setDisable(true);
        lastnameField.setDisable(true);
        salaryField.setDisable(true);
        datePicker.setDisable(true);
        titleField.setDisable(true);
        comissionField.setDisable(true);


        // Update text fields when selecting an employee
        regListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Employee>(){                                   
                @Override                                                     
                public void changed(ObservableValue<? extends Employee> ov,Employee oldValue, Employee newValue){ 
                    if (newValue != null) {

                        // Enable fields
                        typeComboBox.setDisable(false);
                        idField.setDisable(false);
                        nameField.setDisable(false);
                        lastnameField.setDisable(false);
                        salaryField.setDisable(false);
                        datePicker.setDisable(false);

                        // Fill fields
                        fullNameField.setText(newValue.getName() + " " + newValue.getLastname());             
                        idField.setText(String.valueOf(newValue.getId()));
                        nameField.setText(newValue.getName());
                        lastnameField.setText(newValue.getLastname());
                        salaryField.setText(String.valueOf(newValue.getSalary()));  

                        String[] dateParts = newValue.getHireDate().split("-");
                        LocalDate hireDate = LocalDate.of(Integer.parseInt(dateParts[0]),
                                                        Integer.parseInt(dateParts[1]),
                                                        Integer.parseInt(dateParts[2]));
                        datePicker.setValue(hireDate);
                    
                        // Enable/disable according to class
                        if(newValue.getClass() == Manager.class){
                            titleField.setDisable(false);
                            comissionField.setDisable(false);
                            typeComboBox.setValue("Manager");
                            titleField.setText(((Manager)newValue).getEducationLevel());
                            comissionField.setText(String.valueOf(((Manager)newValue).getCommission()));
                        }
                        else{
                            typeComboBox.setValue("Employee");
                            titleField.setDisable(true);
                            comissionField.setDisable(true);
                            titleField.setText("none");
                            comissionField.setText("0");
                        }
                    }
                    else {
                        // Disable fields if null selection
                        typeComboBox.setDisable(true);
                        idField.setDisable(true);
                        nameField.setDisable(true);
                        lastnameField.setDisable(true);
                        salaryField.setDisable(true);
                        datePicker.setDisable(true);
                        titleField.setDisable(true);
                        comissionField.setDisable(true);
                    }
                    
                }
            }
        );  

        // Cell factory for cells in listview
        regListView.setCellFactory(_ -> new ListCell<Employee>(){
            @Override
            protected void updateItem(Employee item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null) 
                    setText(null);
                else                                
                    setText("[ID: " + item.getId() + "] " + item.getName() + " " + item.getLastname());
            }
        });   
    }

}

    
    





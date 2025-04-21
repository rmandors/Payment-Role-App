package controller;

import model.Employee;
import model.EmployeeComparator;
import model.EmployeeList;
import model.Manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller; 
import javax.xml.bind.Unmarshaller; 

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("deprecation")
public class Controller {

    private int newRegCounter = 1;
    private static String[] typeOptions = {"Employee","Manager"};     
    private static String[] formatOptions = {"CSV","XML","JSON","Consola"};       
    private static String[] orderOptions = {"Lastname","Salary","Hire Date"}; 
 
    static ObservableSet<Integer> idSet = FXCollections.observableSet();

    static ObservableList<Employee> employees = FXCollections.observableArrayList();
    private static ObservableList<String> typeItems = FXCollections.observableArrayList(typeOptions);
    private static ObservableList<String> formatItems = FXCollections.observableArrayList(formatOptions);
    private static ObservableList<String> orderItems = FXCollections.observableArrayList(orderOptions);

    @FXML Button deleteReg;
    @FXML Button exportData;
    @FXML Button importData;
    @FXML Button newReg;
    @FXML Button updateReg;

    @FXML ComboBox<String> formatCombobox;
    @FXML ComboBox<String> orderComboBox;
    @FXML ComboBox<String> typeComboBox;

    @FXML DatePicker datePicker;

    @FXML Label fullNameField;

    @FXML ListView<Employee> regListView;

    @FXML TextField idField;
    @FXML TextField comissionField;
    @FXML TextField lastnameField;
    @FXML TextField nameField;
    @FXML TextField salaryField;
    @FXML TextField titleField;

    @FXML
    void deleteReg(ActionEvent event) {

        if (regListView.getSelectionModel().getSelectedItem() == null) return;
        boolean confirm = AlertManager.showConfirmation("Eliminar Registro", "¿Estás seguro que deseas eliminar este registro?");

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

    @FXML
    void updateReg(ActionEvent event){
        Employee selected = regListView.getSelectionModel().getSelectedItem();
        if(selected != null){
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

                    if(orderComboBox.getValue().equals("Lastname")) 
                        SortManager.sortByLastname(); 
                    else if(orderComboBox.getValue().equals("Salary")) 
                        SortManager.sortBySalary();
                    else if(orderComboBox.getValue().equals("Hire Date"))
                        SortManager.sortByHireDate();
                    regListView.refresh();
                }
                catch(IllegalArgumentException e){
                    AlertManager.showWarning("Entrada invalida!", e.getMessage());
                }

            }
            else if (typeComboBox.getValue().equals("Manager")) {
                try {
                    Manager updatedReg = new Manager(selected.getId(), selected.getName(), selected.getLastname(), selected.getDate(), selected.getSalary(), titleField.getText());
                    int index = employees.indexOf(selected);
                    employees.remove(selected);
                    employees.add(index, updatedReg);
                    regListView.refresh();
                    regListView.getSelectionModel().select(index);
                } catch (IllegalArgumentException e) {
                    AlertManager.showWarning("Entrada invalida!", e.getMessage());
                }
            }
            else if (typeComboBox.getValue().equals("Employee")) {
                try {
                    Employee updatedReg = new Employee(selected.getId(), selected.getName(), selected.getLastname(), selected.getDate(), selected.getSalary());
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

    @FXML
    void newReg(ActionEvent event){
        if (idSet.contains(newRegCounter)) {
            newRegCounter = 1;
            int counter = 0;
            while (idSet.contains(newRegCounter) && (counter <= 10000)) {
                counter++;
                newRegCounter++;
            }
            if (counter > 10000) {
                AlertManager.showWarning("Error Fatal!", "Número máximo de IDs alcanzados, elimina los registros anteriores antes de crear uno nuevo");
                return;                
            }
        }
        Employee newEmployee = new Employee(newRegCounter,"Unknown","Unknown",new Date(2025,3,21), 800);
        employees.add(newEmployee);
        regListView.selectionModelProperty().get().select(newEmployee);
    }

    @FXML
    void exportData(ActionEvent event){
        String selectedFormat = formatCombobox.getValue();
        if(selectedFormat.equals("CSV")){
            ExportManager.exportCSV("employeesData2.csv");
        }
        else if(selectedFormat.equals("XML")){
            ExportManager.exportXML("employeesData.xml");
        }
        else if(selectedFormat.equals("JSON")){
            ExportManager.exportJSON("employeesData.json");
        }
        else if(selectedFormat.equals("Consola")){
            ConsoleManager.exportToConsole();
        }
    }

    @FXML
    void importData(ActionEvent event){ 
        
        FileChooser csvChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Archivos CSV", "*.csv");
        Stage mainStage = (Stage) fullNameField.getScene().getWindow();
        
        csvChooser.setTitle("Importar CSV");
        csvChooser.getExtensionFilters().add(csvFilter);
        csvChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectionFile = csvChooser.showOpenDialog(mainStage);

        if (selectionFile != null) {
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

    public void initialize(){

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

        typeComboBox.getItems().addAll(typeItems);
        typeComboBox.setValue("Employee");

        typeComboBox.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.equals("Employee")) {
                    titleField.setEditable(false);
                    titleField.setText("none");
                    comissionField.setText("0");
                } 
                else if (newValue.equals("Manager")) {
                    titleField.setEditable(true);
                    titleField.clear();
                }
            }
        );

        formatCombobox.getItems().addAll(formatItems);
        formatCombobox.setValue("CSV");
        GUIObservers.formatObserver(this, formatCombobox);

        orderComboBox.getItems().addAll(orderItems);
        orderComboBox.setValue("Default");

        orderComboBox.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if(newValue.equals("Lastname")) 
                    SortManager.sortByLastname(); 
                else if(newValue.equals("Salary")) 
                    SortManager.sortBySalary();
                else if(newValue.equals("Hire Date"))
                    SortManager.sortByHireDate();
            }
        );
 
        if (comissionField != null) {
            comissionField.setEditable(false);
        }

        ExportManager.importCSV("employeesData.csv");
        regListView.setItems(employees);

        GUIObservers.idObserver(this, regListView);

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

                        typeComboBox.setDisable(false);
                        idField.setDisable(false);
                        nameField.setDisable(false);
                        lastnameField.setDisable(false);
                        salaryField.setDisable(false);
                        datePicker.setDisable(false);

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

        regListView.setCellFactory(lv -> new ListCell<Employee>() {
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

    
    






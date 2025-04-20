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
    private static String[] formatOptions = {"CSV","XML","JSON"};       
    private static String[] orderOptions = {"Lastname","Salary","Hire Date"}; 
 
    static ObservableSet<Integer> idSet = FXCollections.observableSet();

    private static ObservableList<Employee> employees = FXCollections.observableArrayList();
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
        boolean confirm = showConfirmation("Eliminar Registro", "¿Estás seguro que deseas eliminar este registro?");

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
                    regListView.refresh();
                }
                catch(IllegalArgumentException e){
                    showWarning("Entrada invalida!", e.getMessage());
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
                    showWarning("Entrada invalida!", e.getMessage());
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
                    showWarning("Entrada invalida!", e.getMessage());
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
                showWarning("Error Fatal!", "Número máximo de IDs alcanzados, elimina los registros anteriores antes de crear uno nuevo");
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
            exportCSV("employeesData2.csv");
        }
        else if(selectedFormat.equals("XML")){
            exportXML("employeesData.xml");
        }
        else if(selectedFormat.equals("JSON")){
            exportJSON("employeesData.json");
        }
    }

    @FXML
    void importData(ActionEvent event){ 
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
        importCSV("employeesData.csv");
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

        orderComboBox.getItems().addAll(orderItems);
        orderComboBox.setValue("Default");
 
        if (comissionField != null) {
            comissionField.setEditable(false);
        }

        importCSV("employeesData.csv");
        regListView.setItems(employees);

        GUIObservers.validateObservers(this, regListView);

        // Update text fields when selecting an employee
        regListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Employee>(){                                   
                @Override                                                     
                public void changed(ObservableValue<? extends Employee> ov,Employee oldValue, Employee newValue){ 
                    if (newValue != null) {
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
                            typeComboBox.setValue("Manager");
                            titleField.setText(((Manager)newValue).getEducationLevel());
                            comissionField.setText(String.valueOf(((Manager)newValue).getCommission()));
                        }
                        else{
                            typeComboBox.setValue("Employee");
                            titleField.setText("none");
                            comissionField.setText("0");
                        }
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

    // Reads the Employee data from a CSV file and load it into an ObservableList
    private static void importCSV(String fileName){
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;

            reader.readLine(); // Skip header

            while((line = reader.readLine()) != null){
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String lastname = data[2];
                float salary = Float.parseFloat(data[4]);
                String educationLevel = data[5];

                String[] dateData = data[3].split("-");
                int year = Integer.parseInt(dateData[0]);
                int month = Integer.parseInt(dateData[1]) - 1; // Month is 0-based
                int day = Integer.parseInt(dateData[2]);
                Date hireDate = new Date(year, month, day);

                Employee employee;
                if (educationLevel.equals("none"))
                    employee = new Employee(id, name, lastname, hireDate, salary);
                else
                    employee = new Manager(id, name, lastname, hireDate, salary, educationLevel);
                
                employees.add(employee);                
            }
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // Writes the Employee ObservableList to a CSV file using Formatter
    private static void exportCSV(String fileName){
        try(Formatter output = new Formatter(new File(fileName))){
            output.format("id,name,lastname,hireDate,salary,educationLevel%n");

            for(Employee s : employees){
            if(s.getClass() == Manager.class)
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), ((Manager)s).getEducationLevel());
            else
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), "none");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // Function to show warning alerts
    static void showWarning(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            return true;
        }
        
        return false;

    }

    private static void sortByLastname(){
        employees.sort(new EmployeeComparator());
    }

    private static void sortBySalary(){
        employees.sort(Employee::compareTo);
    }

    private static void sortByHireDate(){
        employees.sort((e1, e2) -> e1.getHireDate().compareTo(e2.getHireDate()));
    }

    private static void exportXML(String fileName){
        try{
            JAXBContext contextObj = JAXBContext.newInstance(EmployeeList.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            EmployeeList employeesList = new EmployeeList(employees);

            marshallerObj.marshal(employeesList, new FileOutputStream(fileName));

            System.out.println("XML file written successfully!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void exportJSON(String fileName){
        try(FileWriter file = new FileWriter(fileName)){
            JSONArray employeesArray = new JSONArray();

            for(Employee e : employees){
                JSONObject employeeDetails = new JSONObject();
                employeeDetails.put("name", e.getName());
                employeeDetails.put("lastname", e.getLastname());
                employeeDetails.put("hireDate", e.getHireDate());
                employeeDetails.put("salary", e.getSalary());
                if(e.getClass() == Manager.class)
                    employeeDetails.put("educationLevel", ((Manager)e).getEducationLevel());
                else
                    employeeDetails.put("educationLevel", "none");
    
                JSONObject employeeObject = new JSONObject();
                if(e.getClass() == Manager.class)
                    employeeObject.put("manager", employeeDetails);
                else
                    employeeObject.put("employee", employeeDetails);
    
                employeesArray.add(employeeObject);
            }
    
            file.write(employeesArray.toJSONString());
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

        private static void exportFinalReport(String filename){
        try(Formatter output = new Formatter(new File(filename))){
            sortByLastname();
            output.format("%-4s | %-10s | %-10s | %-4s | %-10s | %-10s%n", 
                         "ID", "Nombre", "Apellido", "IESS", "Imp. Renta", "Liquido");

            String separator = String.format("%95s", "").replace(" ", "-");
            output.format("%s%n", separator);

            for(Employee s : employees){
                float totalSalary = 0;
                if(s.getClass() == Manager.class)
                    totalSalary = s.getSalary() + ((Manager)s).getCommission();
                else
                    totalSalary = s.getSalary();

                output.format("%-4s | %-10s | %-10s | %-4s | %-10s | %-5s %s%n", s.getId(), 
                              s.getName(), s.getLastname(), calcIESS(totalSalary), calcImpRent(totalSalary), 
                              calcLiquidSalary(totalSalary), salaryToString(calcLiquidSalary(totalSalary)));
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static int calcIESS(float totalSalary){
        float iess = totalSalary * 0.0945f;
        return Math.round(iess);
    }

    public static int calcImpRent(float totalSalary){
        float impRent = 0;
        float anualSalary = totalSalary * 12;

        if(anualSalary >= 0 && anualSalary <= 11902)
            impRent = 0;
        else if(anualSalary > 11902 && totalSalary <= 15159)
            impRent = (anualSalary - 11902) * 0.05f;
        else if(anualSalary > 15159 && totalSalary <= 19682)
            impRent = 163 + (anualSalary - 15159) * 0.1f;
        else if(anualSalary > 19682 && totalSalary <= 26031)
            impRent = 615 + (anualSalary - 19682) * 0.12f;
        else if(anualSalary > 26031 && totalSalary <= 34255)
            impRent = 1377 + (anualSalary - 26031) * 0.15f;
        else if(anualSalary > 34255 && totalSalary <= 45407)
            impRent = 2611 + (anualSalary - 34255) * 0.2f;
        else if(anualSalary > 45407 && totalSalary <= 60450)
            impRent = 4841 + (anualSalary - 45407) * 0.25f;
        else if(anualSalary > 60450 && totalSalary <= 80605)
            impRent = 8602 + (anualSalary - 60450) * 0.3f;
        else if(anualSalary > 80605 && totalSalary <= 107199)
            impRent = 14648 + (anualSalary - 80605) * 0.35f;
        else if(anualSalary > 107199)
            impRent = 23956 + (anualSalary - 107199) * 0.37f;

        impRent = impRent / 12; // Monthly tax
        return Math.round(impRent);
    }

    public static int calcLiquidSalary(float totalSalary){
        return Math.round(totalSalary 
                          - calcIESS(totalSalary) - calcImpRent(totalSalary));
    }

    public static String salaryToString(int salary) {   
        String[] units = {"", "uno", "dos", "tres", "cuatro", "cinco", "seis",
                          "siete", "ocho", "nueve"};

        String[] teens = {"diez", "once", "doce", "trece", "catorce", "quince", 
                          "dieciséis", "diecisiete", "dieciocho", "diecinueve"};

        String[] tens = {"", "", "veinte", "treinta", "cuarenta", "cincuenta", 
                         "sesenta", "setenta", "ochenta", "noventa"};

        String[] hundreds = {"", "ciento", "doscientos", "trescientos", "cuatrocientos", 
                             "quinientos", "seiscientos", "setecientos", "ochocientos", 
                             "novecientos"};
    
        String words = "";

        if(salary == 0) return "cero";
    
        // Thousands
        if(salary >= 1000){
            int thousandPart = salary / 1000;
            
            if (thousandPart == 1)
                words += "mil ";            
            else 
                words += salaryToString(thousandPart) + " mil ";
            
            salary %= 1000;
        }
    
        // Hundreds
        if(salary >= 100){
            int hundredPart = salary / 100;
            
            if (hundredPart == 1 && salary % 100 == 0) 
                words += "cien ";
            else 
                words += hundreds[hundredPart] + " ";

            salary %= 100;
        }
    
        // Tens and Units 
        if(salary >= 20){
            int tenPart = salary / 10;
            words += tens[tenPart];
            
            if (salary % 10 != 0) 
                words += " y " + units[salary % 10];
        } 
        else if(salary >= 10) 
            words += teens[salary - 10];
        else if (salary > 0) 
            words += units[salary];
            
        return words.trim();
    }
}






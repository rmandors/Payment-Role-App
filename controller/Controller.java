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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
 
    private static ObservableList<Employee> employees = FXCollections.observableArrayList();
    private static ObservableList<String> typeItems = FXCollections.observableArrayList(typeOptions);
    private static ObservableList<String> formatItems = FXCollections.observableArrayList(formatOptions);
    private static ObservableList<String> orderItems = FXCollections.observableArrayList(orderOptions);

    @FXML
    private TextField comissionField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button deleteReg;

    @FXML
    private Button export;

    @FXML
    private ComboBox<String> formatCombobox;

    @FXML
    private Label fullNameField;

    @FXML
    private TextField idField;

    @FXML
    private Button importCSV;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField nameField;

    @FXML
    private Button newReg;

    @FXML
    private ComboBox<String> orderComboBox;

    @FXML
    private ListView<Employee> regListView;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button updateReg;
    
    @FXML
    void deleteReg(ActionEvent event) {
        boolean confirm = true;
        if(confirm){
            // Delete the selected employee
            Employee selected = regListView.getSelectionModel().getSelectedItem();
            if (selected != null){
                idField.clear();
                nameField.clear();
                lastnameField.clear();
                salaryField.clear();
                titleField.clear();
                employees.remove(selected);                
            }
        } 
    }

    @FXML
    void newReg(ActionEvent event){
        Employee newEmployee = new Employee(newRegCounter,"Unknown","Unknown",new Date(2025,3,21), 800);
        employees.add(newEmployee);
        regListView.selectionModelProperty().get().select(newEmployee);
        newRegCounter++;
    }

    @FXML
    void export(ActionEvent event){
        String selectedFormat = formatCombobox.getValue();
        if(selectedFormat.equals("CSV")){
            exportCSV("employeesData2.csv");
        }
        else if(selectedFormat.equals("XML")){
            exportXML("employeesData2.xml");
        }
        else if(selectedFormat.equals("JSON")){
            exportJSON("employeesData2.json");
        }
    }

    @FXML
    void employeeTypeSelection(ActionEvent event){
        String s = typeComboBox.getValue();
        if(s.equals("Employee"))
            titleField.setEditable(false);
        else if(s.equals("Manager"))
            titleField.setEditable(true);
    }

    public void initialize(){
        typeComboBox.getItems().addAll(typeItems);
        typeComboBox.setValue("Employee");

        formatCombobox.getItems().addAll(formatItems);
        formatCombobox.setValue("CSV");

        orderComboBox.getItems().addAll(orderItems);
        orderComboBox.setValue("Default");
 
        if (comissionField != null) {
            comissionField.setEditable(false);
        }

        importCSV("employeesData.csv");
        regListView.setItems(employees);

        // Update text fields when selecting an employee
        regListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Employee>(){                                   
                @Override                                                     
                public void changed(ObservableValue<? extends Employee> ov,
                                   Employee oldValue, Employee newValue){           
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
                        titleField.setEditable(true);
                        titleField.setText(((Manager)newValue).getEducationLevel());
                        comissionField.setText(String.valueOf(((Manager)newValue).getCommission()));
                    }
                    else{
                        typeComboBox.setValue("Employee");
                        titleField.setEditable(false);
                        titleField.setText("none");
                        comissionField.setText("0");
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
    private void showWarning(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
}






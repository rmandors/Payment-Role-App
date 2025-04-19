import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

@SuppressWarnings("deprecation")
public class Controller {

    private int newRegCounter = 1;
    private static String[] selectionOptions = {"Employee","Manager"};       

    private static ObservableList<Employee> employees = FXCollections.observableArrayList();
    private static ObservableList<String> items = FXCollections.observableArrayList(selectionOptions);

    @FXML
    private TextField comissionField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button deleteReg;

    @FXML
    private Button export;

    @FXML
    private Label fullNameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField nameField;

    @FXML
    private Button newReg;

    @FXML
    private ListView<Employee> regListView;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<String> typeComboBox;

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
        writeCSV("employeesData2.csv");
    }

    public void initialize(){
        typeComboBox.getItems().addAll(items);
        typeComboBox.setValue("Employee");

        readCSV("employeesData.csv");
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
                  
                  if(newValue.getClass() == Manager.class){
                      titleField.setText(((Manager)newValue).getEducationLevel());
                      comissionField.setText(String.valueOf(((Manager)newValue).getCommission()));
                  }
                  else{
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
    private static void readCSV(String fileName){
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
    private static void writeCSV(String fileName){
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

    private static void sortByLastname(){
        employees.sort(new EmployeeComparator());
    }

    private static void sortBySalary(){
        employees.sort(Employee::compareTo);
    }

    private static void sortByHireDate(){
        employees.sort((e1, e2) -> e1.getHireDate().compareTo(e2.getHireDate()));
    }

}






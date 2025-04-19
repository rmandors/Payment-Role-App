// 21 Abril 2025
// Proyecto Final: Rol de Pagos
// NRC: 2108	
// Raymond Portilla - 00335050
// Nicolas Tovar - 00332988

import java.util.List;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;*/

public class PaymentRole{ //extends Application {
    /*@Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("PaymentRole.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Payment Role");
        primaryStage.setResizable(false);
        primaryStage.show(); 
    }*/
    public static List<Employee> employees = new ArrayList<>();

    public static void main(String[] args){
        //Application.launch(args);
        readCSV("employeesData.csv");
        System.out.println(employees);

        writeCSV("employeesData2.csv");

        readCSV("employeesData2.csv");
        System.out.println(employees);
    }

    // Reads the Employee data from a CSV file and load it into a ArratList.
    @SuppressWarnings("deprecation")
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
                int month = Integer.parseInt(dateData[1]); 
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

    //Writes the Employee list to a CSV file using Formatter
    private static void writeCSV(String fileName){
        try(Formatter output = new Formatter(new File(fileName))){
            output.format("id,name,lastname,hireDate,salary,educationLevel%n");

            for(Employee s : employees){
            if(s.getClass() == Manager.class)
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), s.getHireDate(), s.getSalary(), ((Manager)s).getEducationLevel());
            else
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), s.getHireDate(), s.getSalary(), "none");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}



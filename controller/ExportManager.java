package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Formatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Employee;
import model.EmployeeList;
import model.Manager;

public class ExportManager {

    // Exports employees list to XML
    static void exportXML(String fileName){
        try{
            JAXBContext contextObj = JAXBContext.newInstance(EmployeeList.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            EmployeeList employeesList = new EmployeeList(Controller.employees);

            marshallerObj.marshal(employeesList, new FileOutputStream(fileName));

            AlertManager.showInformation("Archivo creado!", "El archivo XML ha sido exportado correctamente.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Exports employees list to JSON
    @SuppressWarnings({"deprecation", "unchecked"})
    static void exportJSON(String fileName){
        try(FileWriter file = new FileWriter(fileName)){
            JSONArray employeesArray = new JSONArray();

            for(Employee e : Controller.employees){
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
            AlertManager.showInformation("Archivo creado!", "El archivo JSON ha sido exportado correctamente.");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // Exports IESS to 
    static void exportFinalReport(String filename){
        try(Formatter output = new Formatter(new File(filename))){
            SortManager.sortByLastname();
            output.format("%-4s | %-10s | %-10s | %-4s | %-10s | %-10s%n", 
                         "ID", "Nombre", "Apellido", "IESS", "Imp. Renta", "Liquido");

            String separator = String.format("%95s", "").replace(" ", "-");
            output.format("%s%n", separator);

            for(Employee s : Controller.employees){
                float totalSalary = 0;
                if(s.getClass() == Manager.class)
                    totalSalary = s.getSalary() + ((Manager)s).getCommission();
                else
                    totalSalary = s.getSalary();

                output.format("%-4s | %-10s | %-10s | %-4s | %-10s | %-5s %s%n", s.getId(), 
                              s.getName(), s.getLastname(), CalcManager.calcIESS(totalSalary), CalcManager.calcImpRent(totalSalary), 
                              CalcManager.calcLiquidSalary(totalSalary), CalcManager.salaryToString(CalcManager.calcLiquidSalary(totalSalary)));
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // Writes the Employee ObservableList to a CSV file using Formatter
    static void exportCSV(String fileName){
        try(Formatter output = new Formatter(new File(fileName))){
            output.format("id,name,lastname,hireDate,salary,educationLevel%n");

            for(Employee s : Controller.employees){
            if(s.getClass() == Manager.class)
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), ((Manager)s).getEducationLevel());
            else
                output.format("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), "none");
            }
            AlertManager.showInformation("Archivo creado!", "El archivo CSV ha sido exportado correctamente.");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    
    // Reads the Employee data from a CSV file and load it into an ObservableList
    static void importCSV(String fileName){
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
                
                Controller.employees.add(employee);
                //AlertManager.showInformation("Registros importados!", "Se ha importado el archivo CSV correctamente.");                
            }
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }

    static void importCSV(File fileName){
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
                
                Controller.employees.add(employee);                
            }
            AlertManager.showInformation("Registros importados!", "Se ha importado el archivo CSV correctamente.");
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

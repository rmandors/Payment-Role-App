package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Employee;
import model.EmployeeList;
import model.Manager;

public class ExportManager {

    static void exportXML(String fileName){
        try{
            JAXBContext contextObj = JAXBContext.newInstance(EmployeeList.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            EmployeeList employeesList = new EmployeeList(Controller.employees);

            marshallerObj.marshal(employeesList, new FileOutputStream(fileName));

            System.out.println("XML file written successfully!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
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
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

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

}

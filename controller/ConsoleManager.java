package controller;

import java.util.Scanner;

import model.Employee;
import model.Manager;   

public class ConsoleManager {

    // Scaner declaration
    private static final Scanner scanner = new Scanner(System.in);

    // Print registries table
    static void exportToConsole(){
        System.out.println("\nListado de Empleados: ");
        for(int i = 0; i < Controller.employees.size(); i++){
            System.out.println(Controller.employees.get(i).toString());
        }
        return;
    }

    // Search and print by id
    static void searchForUserId(){
        System.out.print("\nIntroduzca el ID del empleado que desea buscar: ");

        try{
            int id = Integer.parseInt(scanner.next());
            boolean found = false;
            int index = -1;

            for(int i = 0; i < Controller.employees.size(); i++) {
                if (Controller.employees.get(i).getId() == id) {
                    found = true;
                    index = i;
                    break;
                }
            }

            if(found){
                Employee s = Controller.employees.get(index);

                System.out.println("\nEl ID ha sido encontrado!");
                System.out.println("\nInformación del empleado: ");
                System.out.println(s.toString());

                System.out.println("\nReporte mensual: ");
                float totalSalary = 0;

                System.out.printf("%-4s | %-10s | %-10s | %-4s | %-10s | %-10s%n", 
                                 "ID", "Nombre", "Apellido", "IESS", "Imp. Renta", "Liquido");

                String separator = String.format("%95s", "").replace(" ", "-");
                System.out.printf("%s%n", separator);

                if(s.getClass() == Manager.class)
                    totalSalary = s.getSalary() + ((Manager)s).getCommission();
                else
                    totalSalary = s.getSalary();

                System.out.printf("%-4s | %-10s | %-10s | %-4s | %-10s | %-5s %s%n", s.getId(), 
                                    s.getName(), s.getLastname(), CalcManager.calcIESS(totalSalary), 
                                    CalcManager.calcImpRent(totalSalary), CalcManager.calcLiquidSalary(totalSalary), 
                                    CalcManager.salaryToString(CalcManager.calcLiquidSalary(totalSalary)));
            }
            else
                System.out.println("\nNo se ha encontrado ningún empleado con el ID: " + id);
        }
        catch(NumberFormatException e){
            System.out.println("\nEl ID debe ser un número entero positivo.");
        }   
    }
    
}

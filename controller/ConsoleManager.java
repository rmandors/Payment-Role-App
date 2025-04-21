package controller;

import java.util.Scanner;

import model.Employee;
import model.Manager;   

public class ConsoleManager {
    
    static void exportToConsole(){
        System.out.println("\nListado de Empleados:");
        for (int i = 0; i < Controller.employees.size(); i++) {
            System.out.println(Controller.employees.get(i).toString());
        }
        return;
    }

    static void searchForUserId(){
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Introduzca el ID del empleado que desea buscar:");

            int id = Integer.parseInt(scanner.nextLine());
            boolean found = false;

            for(int i = 0; i < Controller.employees.size(); i++) {
                if(Controller.employees.get(i).getId() == id) {
                    System.out.println(Controller.employees.get(i).toString());
                    found = true;
                    break;
                }
            }

            if(found){
                Employee s = Controller.employees.get(id);

                System.out.println("El usuario ha sido encontrado!");
                System.out.println("Información del empleado: ");
                System.out.println(s.toString());

                System.out.println("\nReporte mensual:");
                if(s.getClass() == Manager.class)
                    System.out.printf("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), ((Manager)s).getEducationLevel());
                else
                    System.out.printf("%s,%s,%s,%s,%s,%s%n", s.getId(), s.getName(), s.getLastname(), 
                             s.getHireDate(), s.getSalary(), "none");
            }
            else
                System.out.println("No se ha encontrado ningún empleado con el ID: " + id);
        }
    }

}

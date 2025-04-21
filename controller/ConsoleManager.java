package controller;

import java.util.Scanner;   

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
                if (Controller.employees.get(i).getId() == id) {
                    System.out.println(Controller.employees.get(i).toString());
                    found = true;
                }
            }

            if(found){
                System.out.println("El usuario ha sido encontrado!");
                System.out.println("Informacion del empleado: ");
                System.out.println(Controller.employees.get(id).toString());
            }
            else
                System.out.println("No se ha encontrado ningún empleado con el ID: " + id);
        }
    }

}

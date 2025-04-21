package controller;

import java.util.Comparator;

import model.Employee;
import model.EmployeeComparator;

public class SortManager {
    
    static void sortByLastname(){
        Controller.employees.sort(new EmployeeComparator());
    }

    static void sortBySalary(){
        Controller.employees.sort(Comparator.comparing(Employee::getSalary));
    }

    static void sortByHireDate(){
        Controller.employees.sort((Comparator.comparing(Employee::getHireDate)));
    }


}
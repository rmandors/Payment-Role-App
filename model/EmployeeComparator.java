package model;
import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee>{
    
    public int compare(Employee e1, Employee e2){
    int comparison = e1.getLastname().compareTo(e2.getLastname());

        if(comparison > 0)
            return 1;
        else if(comparison == 0)
            return 0;
        else
            return -1;
    }
}
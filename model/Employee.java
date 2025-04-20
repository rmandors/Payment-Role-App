package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;  

@SuppressWarnings("deprecation")
@XmlRootElement(name="employee") 
public class Employee implements Comparable<Employee>{
    private IntegerProperty id = new SimpleIntegerProperty();
    private String name;
    private String lastname;
    private Date hireDate;
    private float salary;
    
    public Employee(){}

    public Employee(int i, String n, String ln, Date hd, float s){
        setId(i);
        setName(n);
        setLastname(ln);
        setHireDate(hd);
        setSalary(s);
    }

    public void setId(int i){
        if(i > 0 && i < 10000)
            id.set(i);
        else
            throw new IllegalArgumentException("Invalid ID. Must be beetween: 1-9999!");
    }

    @XmlElement
    public int getId(){
        return id.get();
    }

    public void setName(String n){
        if(n.length() > 0)
            name = n;
        else
            throw new IllegalArgumentException("Invalid Name length!");
    }

    @XmlElement
    public String getName(){
        return name;
    }

    public void setLastname(String ln){
        if(ln.length() > 0)
            lastname = ln;
        else
            throw new IllegalArgumentException("Invalid Lastname length!");
    }

    @XmlElement
    public String getLastname(){
        return lastname;
    }

    public void setHireDate(Date hd){
        if(hd.getYear() > 1900)
            hireDate = hd;
        else
            throw new IllegalArgumentException("Invalid Hire Date!");
    }

    @XmlElement
    public String getHireDate(){
        return hireDate.getYear() + "-" +
               (hireDate.getMonth() + 1)+ "-" +
               hireDate.getDate();
    }

    public void setSalary(float s){
        if(s >= 800 && s <= 3500)
            salary = s;
        else
            throw new IllegalArgumentException("Invalid Salary. Must be beetween: 800-3500!");
    }

    public IntegerProperty idProperty() {
        return id;
    };

    @XmlElement
    public float getSalary(){
        return salary;
    }

    @Override
    public String toString(){
        return "[ID: " + getId() + ", " +
               "Name: " + getName() + ", " +
               "Hire Date: " + getHireDate() + ", " +
               "Lastname: " + getLastname() + ", " +               
               "Salary: " + getSalary() + "]";
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Employee right = (Employee) obj;
        if(getSalary() == right.getSalary() && 
           getHireDate() == right.getHireDate())
            return true;
        else
            return false;
    }

    @Override
    public int compareTo(Employee right){
        if(getSalary() > right.getSalary())
            return 1;
        else if(getSalary() == right.getSalary())
            return 0;
        else 
            return -1;            
    }
}

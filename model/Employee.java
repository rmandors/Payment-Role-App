package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;  

@SuppressWarnings("deprecation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="employee")
@XmlSeeAlso(Manager.class)
public class Employee implements Comparable<Employee>{
    
    // Properties
    @XmlTransient
    private IntegerProperty id = new SimpleIntegerProperty();

    // Atributes
    private String name;
    private String lastname;
    private Date hireDate;
    private float salary;
    
    public Employee(){} // Default constructor

    // Constructor
    public Employee(int i, String n, String ln, Date hd, float s){
        setId(i);
        setName(n);
        setLastname(ln);
        setHireDate(hd);
        setSalary(s);
    }

    // Setters and getters

    public void setId(int i){
        if(i > 0 && i < 10000)
            id.set(i);
        else
            throw new IllegalArgumentException("ID inválido. Su valor debe estar entre: 1-9999!");
    }

    @XmlElement(name="id")
    public int getId(){
        return id.get();
    }

    public void setName(String n){
        if(n.length() > 0)
            name = n;
        else
            throw new IllegalArgumentException("Nombre inválido!");
    }

    @XmlElement(name="name")
    public String getName(){
        return name;
    }

    public void setLastname(String ln){
        if(ln.length() > 0)
            lastname = ln;
        else
            throw new IllegalArgumentException("Apellido inválido!");
    }

    @XmlElement(name="lastname")
    public String getLastname(){
        return lastname;
    }

    public void setHireDate(Date hd){
        if(hd.getYear() > 1975)
            hireDate = hd;
        else
            throw new IllegalArgumentException("Fecha de contratación inválida!");
    }

    @XmlElement(name="hireDate")
    public String getHireDate(){
        return hireDate.getYear() + "-" +
               (hireDate.getMonth() + 1)+ "-" +
               hireDate.getDate();
    }

    public Date getDate(){
        return hireDate;
    }

    public void setSalary(float s){
        if(s >= 800 && s <= 3500)
            salary = s;
        else
            throw new IllegalArgumentException("Salario inválido. Su valor debe estar entre: 800-3500!");
    }

    public IntegerProperty idProperty() {
        return id;
    };

    @XmlElement(name="salary")
    public float getSalary(){
        return salary;
    }

    // toString method
    @Override
    public String toString(){
        return "[ID: " + getId() + ", " +
               "Name: " + getName() + ", " +
               "Hire Date: " + getHireDate() + ", " +
               "Lastname: " + getLastname() + ", " +               
               "Salary: " + getSalary() + "]";
    }

    // Equality and comparable
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
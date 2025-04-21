package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;
 

@XmlRootElement(name="manager")
@XmlAccessorType(XmlAccessType.PROPERTY)

public class Manager extends Employee {

    // Atributes
    private String educationLevel;
    private final float commissionRate = 0.05f;

    // Default constructor
    public Manager(){
        super();
    }

    // Constructor
    public Manager(int i, String n, String ln, Date hd, float s, String el){
        super(i, n, ln, hd, s);
        setEducationLevel(el);
    }

    // Setters and getters

    public void setEducationLevel(String el){
        if(el.length() > 0)
        educationLevel = el;
        else
            throw new IllegalArgumentException("Nivel de educación inválido!");
    }

    @XmlElement(name="educationLevel")
    public String getEducationLevel(){
        return educationLevel;
    }

    public float getCommission(){
        return Math.round(getSalary() * commissionRate * 100) / 100.0f;
    }

    // toString method
    @Override 
    public String toString(){
        return super.toString().replace("]", "") 
               + ", " + "Education Level: " + getEducationLevel() 
               + ", " + "Commission: " + getCommission() + "]";
    }

    // Comparable
    @Override
    public int compareTo(Employee right){
        float totalPayment = getSalary() + getCommission();
        float rightTotalPayment = 0;

        if(right.getClass() != Manager.class)
            rightTotalPayment = right.getSalary() + ((Manager)right).getCommission();
        else
            rightTotalPayment = right.getSalary();            

        if(totalPayment > rightTotalPayment)
            return 1;
        else if(totalPayment == rightTotalPayment)
            return 0;
        else 
            return -1;
    }
}

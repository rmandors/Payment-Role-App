package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement(name="manager") 
public class Manager extends Employee {
    private String educationLevel;
    private final float commissionRate = 0.05f;

    public Manager(int i, String n, String ln, Date hd, float s, String el){
        super(i, n, ln, hd, s);
        setEducationLevel(el);
    }

    public void setEducationLevel(String el){
        if(el.length() > 0)
        educationLevel = el;
        else
            throw new IllegalArgumentException("Invalid Education Level!");
    }

    @XmlElement
    public String getEducationLevel(){
        return educationLevel;
    }

    public float getCommission(){
        return Math.round(getSalary() * commissionRate * 100) / 100f;
    }

    @Override 
    public String toString(){
        return super.toString().replace("]", "") 
               + ", " + "Education Level: " + getEducationLevel() + "]";
    }

    @Override
    public int compareTo(Employee right){
        float totalPayment = getSalary() + getCommission(); ;
        float rightTotalPayment = right.getSalary() + ((Manager)right).getCommission();

        if(totalPayment > rightTotalPayment)
            return 1;
        else if(totalPayment == rightTotalPayment)
            return 0;
        else 
            return -1;
    }
}

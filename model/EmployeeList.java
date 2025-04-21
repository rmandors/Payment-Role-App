package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeList{
    private List<Employee> employees;

    public EmployeeList(){} 

    public EmployeeList(List<Employee> e){
        setEmployees(e);
    }

    public void setEmployees(List<Employee> e){
        employees = e;
    }

    @XmlElements({
        @XmlElement(name="employee", type=Employee.class),
        @XmlElement(name="manager", type=Manager.class)
    })
    public List<Employee> getEmployees(){
        return employees;
    }
}

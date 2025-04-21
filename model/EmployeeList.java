package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Manager.class})
public class EmployeeList{

    // List of employees for JAXB use

    @XmlElements({
        @XmlElement(name="employee", type=Employee.class),
        @XmlElement(name="manager", type=Manager.class)
    })

    private List<Employee> employees;

    public EmployeeList(){} 

    public EmployeeList(List<Employee> e){
        this.employees = e;
    }

}

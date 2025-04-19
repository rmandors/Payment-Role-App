import java.util.Date;

public class Manager extends Employee {
    private String educationLevel;
    private final float commission = 0.05f;

    public Manager(int i, String n, String ln, Date hd, float s, String el){
        super(i, n, ln, hd, s);
        setEducationLevel(el);
    }

    public void setEducationLevel(String el){
        if(el != null)
        educationLevel = el;
        else
            throw new IllegalArgumentException("Invalid Title!");
    }

    public String getEducationLevel(){
        return educationLevel;
    }

    @Override 
    public String toString(){
        return super.toString().replace("]", "") 
               + ", " + "Education Level: " + getEducationLevel() + "]";
    }

    @Override
    public int compareTo(Employee right){
        float totalPayment = getSalary() 
                             + getSalary() * commission ;
        float rightTotalPayment = right.getSalary() 
                                  + right.getSalary() * commission;

        if(totalPayment > rightTotalPayment)
            return 1;
        else if(totalPayment == rightTotalPayment)
            return 0;
        else 
            return -1;
    }
}

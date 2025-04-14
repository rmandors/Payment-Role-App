import java.util.Date;

public class Manager extends Employee {
    private String title;
    private final float commission = 0.05f;

    public Manager(String n, String ln, Date hd, int i, float s, String t){
        super(n, ln, hd, i , s);
        setTitle(t);
    }

    public void setTitle(String t){
        if(t != null)
            title = t;
        else
            throw new IllegalArgumentException("Invalid Title!");
    }

    public String getTitle(){
        return title;
    }

    @Override 
    public String toString(){
        return super.toString().replace("]", "") 
               + ", " + "Title: " + getTitle() + "]";
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

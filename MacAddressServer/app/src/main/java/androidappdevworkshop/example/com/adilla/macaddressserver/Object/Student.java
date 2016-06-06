package androidappdevworkshop.example.com.adilla.macaddressserver.Object;

/**
 * Created by Adilla on 17/4/2016.
 */
public class Student {
    private String name;
    private String matricNo;
    private String MACAddress;
    private int id;

    public Student(String name, String matricNo){
        this.name = name;
        this.matricNo = matricNo;
    }

    /**
     * default constructor of student
     */
    public Student(){

    }

    public String getName(){return name;}

    public String getMatricNo(){return matricNo;}

    public void setName(String name){
        this.name = name;
    }

    public void setMatricNo(String matricNo){
        this.matricNo = matricNo;
    }

    public void setMACAddress(String MACAddress){
        this.MACAddress = MACAddress;
    }

    public String getMACAddress(){
        return MACAddress;
    }

    public int getId(){return id;}

    public void setId(int id){ this.id = id;}
}

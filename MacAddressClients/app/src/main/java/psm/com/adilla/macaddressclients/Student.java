package psm.com.adilla.macaddressclients;

/**
 * This is class for collective of student information
 * Created by Adilla on 13/4/2016.
 */
public class Student {

    private String name;
    private String matricNo;
    private String MACAddress;
    private String ipAdd;

    public Student(String name, String matricNo){
        this.name = name;
        this.matricNo = matricNo;
    }

    /**
     * default constructor of student
     */
    public Student(){

    }

    public String getName(){
        return name;
    }

    public String getMatricNo(){
        return matricNo;
    }

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

    public void setIpAdd(String ipAdd){ this.ipAdd = ipAdd;}

    public String getIpAdd(){ return ipAdd; }

}

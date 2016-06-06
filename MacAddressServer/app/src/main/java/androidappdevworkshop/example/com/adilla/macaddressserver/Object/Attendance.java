package androidappdevworkshop.example.com.adilla.macaddressserver.Object;

/**
 * Created by Adilla on 14/5/2016.
 */
public class Attendance {

    private int attendanceId; // auto increment
    private String matricNo; // student
    private String type;  // lecture and lab
    private String attendance;  // present and absent
    private String subjectCode; // subject
    private String dateTime;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    /**
     * Constructor
     */
    public Attendance(){

    }

    public void setAttendanceId(int attendanceId){
        this.attendanceId = attendanceId;
    }

    public int getAttendanceId(){
        return attendanceId;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAttendance(){
        return attendance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMatricNo(String matricNo){
        this.matricNo = matricNo;
    }

    public String getMatricNo(){
        return matricNo;
    }

    public void setSubjectCode(String subjectCode){
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode(){
        return subjectCode;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public String getDateTime(){
        return dateTime;
    }

    /**
     * this method to get the datetime format
     * @return
     */
    public void setDateFormatNow() {

    }

}

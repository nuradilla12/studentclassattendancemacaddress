package androidappdevworkshop.example.com.adilla.macaddressserver.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Student;

/**
 * Table that lsit the data of registered student
 * Created by Adilla on 17/4/2016.
 */
public class TableStudent extends SQLiteOpenHelper {

    public static final String dbName ="dbAttendanceStudent";
    public static final int dbVersion =1;
    public static final String tblName = "student";
    public static final String colMatricNo = "matricNo";
    public static final String colName = "name";
    public static final String colMACAddress = "macAddress";
    public static final String dbCreate = "CREATE TABLE IF NOT EXISTS "+ tblName +
            " ("+ colMatricNo +" VARCHAR PRIMARY KEY NOT NULL UNIQUE, "+ colName +" VARCHAR, "
            + colMACAddress +" VARCHAR);";

    public TableStudent(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblName);
        onCreate(db);
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(colMatricNo, student.getMatricNo());
        cv.put(colName, student.getName());
        cv.put(colMACAddress, student.getMACAddress());

        /*String insert = "INSERT INTO "+ tblName +" VALUES ("+ student.getMatricNo() +", "
                + student.getName() +", "+ student.getMACAddress() +");";
        db.execSQL(insert);*/
        // insert sql

        db.insert(tblName, null, cv);
        db.close();
    }

    public boolean verifyStudentRegistered(String macAdd){

        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT "+ colMACAddress +" FROM "+ tblName +" WHERE "+ colMACAddress
                +" = '"+ macAdd +"'";
        System.out.println("MainActivity: "+select);
        Cursor cr = db.rawQuery(select, null);
        boolean recorded;
        if(cr.moveToFirst()){
            recorded =  true;
        }else {
            recorded = false;
        }
        return recorded;
    }

    public Student getStudent(){

        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tblName +"";
        Cursor cr = db.rawQuery(select, null);
        Student student = new Student();
        if(cr.moveToFirst()){
            do{
                student.setMatricNo(cr.getString(0));
                student.setName(cr.getString(1));
                student.setMACAddress(cr.getString(2));
            }while(cr.moveToNext());
        }

        return student;
    }
}

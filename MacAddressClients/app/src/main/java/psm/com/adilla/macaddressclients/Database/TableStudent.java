package psm.com.adilla.macaddressclients.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import psm.com.adilla.macaddressclients.Student;

/**
 * This class used form database usage that save data in local phone.
 * Created by Adilla on 13/4/2016.
 * @extends SQLiteOpenHelper
 */
public class TableStudent extends SQLiteOpenHelper{

    public static final String dbName ="dbStudent";
    public static final int dbVersion = 1;
    public static final String tblName = "student";
    public static final String colMatricNo = "MatricNo";
    public static final String colName = "Name";
    public static final String colMACAddress = "MACAddress";
    public static final String colIpAdd ="IpAddress";
    public static final String dbCreate = "CREATE TABLE IF NOT EXISTS "+ tblName +"('"+ colMatricNo +
            "' VARCHAR, '"+ colName +"' VARCHAR, '" + colMACAddress + "' VARCHAR, '" + colIpAdd + "' VARCHAR," +
            " PRIMARY KEY ('"+ colMatricNo +"'));";

    public TableStudent(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS");
        onCreate(db);
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colMatricNo, student.getMatricNo());
        cv.put(colName, student.getName());
        cv.put(colMACAddress, student.getMACAddress());
        cv.put(colIpAdd, student.getIpAdd());

        // insert sql
        db.insert(tblName, null, cv);
        db.close();
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
                student.setIpAdd(cr.getString(3));
            }while(cr.moveToNext());
        }
        return student;
    }

    public boolean init(){
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT DISTINCT "+ colMatricNo +" FROM " + tblName +"";
        Cursor cr = db.rawQuery(count, null);
        boolean rowExist;
        if(cr.getCount() > 0){
            rowExist = true;
        }else
            rowExist = false;

        cr.close();
        return rowExist;
    }

    public int updateIpAddress(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colIpAdd, student.getIpAdd());

        return db.update(tblName, cv, colMatricNo + " = ?", new String[]{student.getMatricNo()});
    }
}

package androidappdevworkshop.example.com.adilla.macaddressserver.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Attendance;

/**
 * Created by Adilla on 14/5/2016.
 */

// validate attendance on wheter it already attend

public class TableAttendance extends SQLiteOpenHelper{

    public static final String dbName ="dbAttendanceStudent";
    public static final int dbVersion = 1;
    public static final String tblName = "attendance";
    //public static final String colId = "id";
    public static final String colSubjectCode = "SubjectCode";
    public static final String colType = "Type";
    public static final String colDateTime = "DateTime";
    public static final String colMatricNo = "MatricNo";
    public static final String colAttend = "Attendance";
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String dbCreate = "CREATE TABLE IF NOT EXISTS "+ tblName +"" +
            "("+colSubjectCode+" VARCHAR REFERENCES "+TableSubject.tblName+ "("+TableSubject.colCode+"), "
            +colType+" VARCHAR, " +colDateTime+" DATETIME, "+ colAttend +" VARCHAR, "
            +colMatricNo+" VARCHAR REFERENCES " + TableStudent.tblName + "("+TableStudent.colMatricNo+"));";

    public TableAttendance(Context context){
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



    public void attendClass(Attendance attendance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubjectCode, attendance.getSubjectCode());
        cv.put(colType, attendance.getType());
        cv.put(colMatricNo, attendance.getMatricNo());
        cv.put(colDateTime, attendance.getDateTime());
        cv.put(colAttend, attendance.getAttendance());

        // insert sql
        db.insert(tblName, null, cv);
        db.close();
    }

    // add query for count total attendance
    public int calculateTotal(){
        int count = 0;

        String strCount = "SELECT COUNT FROM "+ TableAttendance.tblName +" WHERE";

        return count;
    }

}

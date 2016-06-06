package androidappdevworkshop.example.com.adilla.macaddressserver.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Subject;

/**
 * Created by Adilla on 20/5/2016.
 */
public class TableSubject extends SQLiteOpenHelper {
    public static final String dbName ="dbAttendanceStudent";
    public static final int dbVersion = 1;
    public static final String tblName = "subject";
    public static final String colCode = "code";
    public static final String colName = "name";
    public static final String dbCreate = "CREATE TABLE IF NOT EXISTS "+ tblName +"("+ colCode +
            " VARCHAR PRIMARY KEY NOT NULL UNIQUE, "+ colName +" VARCHAR);";

    TableLecturer tblLecturer;
    TableStudent tblStudent;
    TableAttendance tblAttendance;

    public TableSubject(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(dbCreate);
        db.execSQL(tblLecturer.dbCreate);
        db.execSQL(tblStudent.dbCreate);
        db.execSQL(tblAttendance.dbCreate);
        Log.d("FLAG", dbCreate);
        Log.d("FLAG", String.valueOf(db.getVersion()));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblName);
        db.execSQL("DROP TABLE IF EXISTS " + tblStudent.tblName);
        db.execSQL("DROP TABLE IF EXISTS " + tblAttendance.tblName);
        onCreate(db);
    }

    public void addSubject(Subject subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(colCode, subject.getCode());
        cv.put(colName, subject.getName());

        // insert sql
        db.insert(tblName, null, cv);
        db.close();
    }

    public List<String> getAllSubject(){

        List<String> subjects = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM subject";
        Cursor cr = db.rawQuery(select, null);
        if(cr.moveToFirst()){
            do{
                subjects.add(cr.getString(0) +"- "+ cr.getString(1));
            }while(cr.moveToNext());
        }
        // closing connection
        cr.close();
        db.close();

        return subjects;
    }

    public boolean checkSubject(){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + tblName +"";
        Cursor cr = db.rawQuery(select, null);
        if(cr.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public String verifySubject(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + tblName +" WHERE "+ colCode +" = '"+ code +"'";
        Cursor cr = db.rawQuery(select, null);
        boolean codeStore;
        if(cr.moveToFirst()){
            return code;
        }else{
            code = "";
        }
        return code;
    }
}

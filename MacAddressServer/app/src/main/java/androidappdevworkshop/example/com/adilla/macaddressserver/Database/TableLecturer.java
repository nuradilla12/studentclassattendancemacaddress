package androidappdevworkshop.example.com.adilla.macaddressserver.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Lecturer;

/**
 * Created by Adilla on 22/5/2016.
 */
public class TableLecturer extends SQLiteOpenHelper {
    public static final String dbName ="dbAttendanceStudent";
    public static final int dbVersion = 1;
    public static final String tblName = "lecturer";
    public static final String colId = "userId";
    public static final String colName = "Name";
    public static final String dbCreate = "CREATE TABLE IF NOT EXISTS "+ tblName +"("+ colId +
            " VARCHAR PRIMARY KEY NOT NULL UNIQUE, "+ colName +" VARCHAR);";

    public TableLecturer(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(dbCreate);
        Log.d("FLAG", dbCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblName);
        this.onCreate(db);
    }

    public void registerLecturer(Lecturer lecturer){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        String insert = "INSERT INTO "+ tblName+" VALUES ('"+ lecturer.getUserId() +"', '"
                + lecturer.getName() +"');";
        db.execSQL(insert);
    }

    public boolean getLecturer(){

        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT DISTINCT "+ colId +" FROM " + tblName + " ";
        Cursor cr = db.rawQuery(select, null);
        boolean rowExist;
        if(cr.getCount() > 0){
            rowExist = true;
        }else {
            rowExist = false;
        }
        cr.close();
        return rowExist;
    }

    public Lecturer getDetailLecturer(){

        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + tblName + " ";
        Cursor cr = db.rawQuery(select, null);
        Lecturer lecturer = new Lecturer();
        if(cr.moveToFirst()){
           do{
               lecturer.setUserId(cr.getString(0));
               lecturer.setName(cr.getString(1));
           }while(cr.moveToNext());
        }
        cr.close();
        return lecturer;
    }
}

package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableStudent;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class StudentListActivity extends AppCompatActivity {

    private static final String strAttName = TableStudent.colName;
    private static final String strAttMatricNo = TableStudent.colMatricNo;
    private static final String strAttMacAdd = TableStudent.colMACAddress;

    Toolbar toolBar;
    TableStudent dbStudent;
    ListView listViewStudent;
    ArrayList<HashMap<String, String>> arrayListStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dbStudent = new TableStudent(getApplicationContext());
        listViewStudent = (ListView)findViewById(R.id.listViewStudent);
        arrayListStudent = new ArrayList<HashMap<String, String>>();

        Runnable run = new Runnable() {
            @Override
            public void run() {

                String strSql = "SELECT * FROM " + TableStudent.tblName;
                Cursor currExp = dbStudent.getReadableDatabase().rawQuery(strSql, null);

                while(currExp.moveToNext()){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(strAttName, currExp.getString(currExp.getColumnIndex(TableStudent.colName)));
                    map.put(strAttMatricNo, currExp.getString(currExp.getColumnIndex(TableStudent.colMatricNo)));
                    //map.put(strAttMacAdd, currExp.getString(currExp.getColumnIndex(TableStudent.colMACAddress)));

                    arrayListStudent.add(map);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(StudentListActivity.this, arrayListStudent,
                                R.layout.listviewofstudent, new String[]{strAttMatricNo
                                ,strAttName}, new int [] { R.id.textViewMatric,
                                R.id.textViewName});

                        listViewStudent.setAdapter(adapter);
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

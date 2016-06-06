package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableAttendance;
import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableStudent;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class AttendanceListActivity extends AppCompatActivity {

    private static final String strAttMatricNo = TableAttendance.colMatricNo;
    private static final String strAttName = TableStudent.colName;
    private static final String strAttDateTime = TableAttendance.colDateTime;

    Toolbar toolBar;
    TableAttendance tblAttendance;
    TableStudent tblStudent;
    EditText editTextSearch;
    ListView listViewAttendance;
    ArrayList<HashMap<String, String>> arrayListAttendance;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTextSearch = (EditText)findViewById(R.id.editTextSearch);
        tblAttendance = new TableAttendance(getApplicationContext());
        listViewAttendance = (ListView)findViewById(R.id.listViewAttendance);
        arrayListAttendance = new ArrayList<HashMap<String, String>>();

        Runnable run = new Runnable() {
            @Override
            public void run() {

            String strSql = "SELECT * FROM " + TableAttendance.tblName +","+ TableStudent.tblName
                    + " WHERE " + TableAttendance.tblName+"."+ TableAttendance.colMatricNo
                    +" = "+TableStudent.tblName+ "." + TableStudent.colMatricNo +"";

             // add query for count total attendance
             String strCount = "SELECT COUNT FROM "+ TableAttendance.tblName +" WHERE";

            Log.d("QUERY", strSql);

                Cursor currExp = tblAttendance.getReadableDatabase().rawQuery(strSql, null);
                while(currExp.moveToNext())
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(strAttMatricNo, currExp.getString(currExp.getColumnIndex(TableAttendance.colMatricNo)));
                    map.put(strAttDateTime, currExp.getString(currExp.getColumnIndex(TableAttendance.colDateTime)));
                    map.put(strAttName, currExp.getString(currExp.getColumnIndex(TableStudent.colName)));

                    arrayListAttendance.add(map);
                    Log.i("DATA:", String.valueOf(map));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new SimpleAdapter(AttendanceListActivity.this, arrayListAttendance,
                                R.layout.listviewinfoattendance, new String[]{strAttMatricNo
                                ,strAttDateTime,strAttName}, new int [] { R.id.textViewMatric,
                                R.id.textViewDateTime, R.id.textViewName});

                        listViewAttendance.setAdapter(adapter);

                        editTextSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                ((SimpleAdapter)AttendanceListActivity.this.adapter).getFilter().filter(s);
                                int startPos = s.toString().toLowerCase(Locale.US).indexOf(s.toString().toLowerCase());
                                int endPos = startPos + s.length();

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
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

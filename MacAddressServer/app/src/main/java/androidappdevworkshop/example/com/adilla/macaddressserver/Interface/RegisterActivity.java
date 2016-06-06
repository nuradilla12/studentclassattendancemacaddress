package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableLecturer;
import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableSubject;
import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Lecturer;
import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Subject;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private EditText editTextName;
    private EditText editTextId;
    private Button btnSubject;
    private static final String strCode = TableSubject.colCode;
    private static final String strName = TableSubject.colName;

    TableLecturer tblLecturer;
    TableSubject tblSubject;
    ListView listViewSubject;
    TextView tvList;
    ArrayList<HashMap<String, String>> arrayListSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        editTextId = (EditText)findViewById(R.id.editTextId);
        editTextName = (EditText)findViewById(R.id.editTextName);
        btnSubject = (Button)findViewById(R.id.buttonSubject);
        listViewSubject = (ListView)findViewById(R.id.listViewSubject);
        tvList =(TextView)findViewById(R.id.textViewList);

        arrayListSubject = new ArrayList<HashMap<String, String>>();
        tblLecturer = new TableLecturer(getApplicationContext());

        tblSubject = new TableSubject(getApplicationContext());
        displayListSubject();
        btnSubject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSubject();
            }
        });

        if(tblLecturer.getLecturer() == true){
            Intent gotoAttendance = new Intent(RegisterActivity.this, CreateAttendanceActivity.class);
            startActivity(gotoAttendance);
        }
    }

    /**
     * this method to save registration lecturer
     * @param view
     */
    public void saveInformation(View view){

        if(verify()) {
            Runnable run = new Runnable() {
                public void run() {

                Lecturer lecturer = new Lecturer();
                lecturer.setUserId(editTextId.getText().toString());
                lecturer.setName(editTextName.getText().toString());
                tblLecturer.registerLecturer(lecturer);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Successful: " +
                                        editTextId.getText().toString() + "|" + editTextName.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        Intent gotoCreateAttendance = new Intent(RegisterActivity.this,
                                CreateAttendanceActivity.class);
                        startActivity(gotoCreateAttendance);
                        finish();
                    }
                });
                }
            };
            Thread thr = new Thread(run);
            thr.start();
        }
    }

    /**
     * this method to display all the subject details
     */
    public void displayListSubject(){

        try{
            if(tblSubject.checkSubject()== true){

                Runnable run = new Runnable() {
                    @Override
                    public void run() {

                        String strSql = "SELECT * FROM " + TableSubject.tblName;
                        Cursor currExp = tblSubject.getReadableDatabase().rawQuery(strSql, null);
                        arrayListSubject = new ArrayList<HashMap<String, String>>();

                        while(currExp.moveToNext()){
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(strCode, currExp.getString(currExp.getColumnIndex(TableSubject.colCode)));
                            map.put(strName, currExp.getString(currExp.getColumnIndex(TableSubject.colName)));
                            arrayListSubject.add(map);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListAdapter adapter = new SimpleAdapter(RegisterActivity.this, arrayListSubject,
                                        R.layout.listviewsubject, new String[]{strCode,strName},
                                        new int [] { R.id.textViewCode, R.id.textViewName});

                                listViewSubject.setAdapter(adapter);
                            }
                        });
                    }
                };
                Thread thr = new Thread(run);
                thr.start();
                tvList.setText("List of subjects");
             }else{
                tvList.setText("None of list of subjects");
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * this method is alert dialog that allow user insert subject detail in database
     */
    public void insertSubject(){

        LayoutInflater layout = LayoutInflater.from(RegisterActivity.this);
        View promptView = layout.inflate(R.layout.input_dialog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        alertDialog.setView(promptView);

        final EditText etCode = (EditText)promptView.findViewById(R.id.etCode);
        final EditText etName = (EditText)promptView.findViewById(R.id.etName);
        alertDialog.setTitle("Insert subject information:");
        alertDialog.setIcon(R.drawable.ic_info_black_24dp);

        alertDialog.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (etCode.getText().toString().equals("") || etName.getText().toString().equals("")) {
                            if (etCode.getText().toString().equals("") && etName.getText().toString().equals("")) {
                                Toast.makeText(RegisterActivity.this, "Please insert subject code" +
                                        " and name", Toast.LENGTH_SHORT).show();
                            } else if (etCode.getText().toString().equals("")) {
                                Toast.makeText(RegisterActivity.this, "Please insert subject code",
                                        Toast.LENGTH_SHORT).show();
                            } else if (etName.getText().toString().equals("")) {
                                Toast.makeText(RegisterActivity.this, "Please insert subject name",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (etCode.getText().toString().trim().length() > 9) {
                            Toast.makeText(RegisterActivity.this, "Subject code must be at most 9",
                                    Toast.LENGTH_SHORT).show();
                        } else if (etCode.getText().toString().equals(tblSubject.verifySubject(
                                etCode.getText().toString()))) {
                            Toast.makeText(RegisterActivity.this, "Duplicate code subject! Please insert subject code",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            etCode.setAllCaps(true);
                            Subject subject = new Subject();
                            subject.setCode(etCode.getText().toString());
                            subject.setName(etName.getText().toString());
                            tblSubject.addSubject(subject);

                            Toast.makeText(getApplicationContext(), "Successful: " +
                                            etCode.getText().toString() + "|" + etName.getText().toString(),
                                    Toast.LENGTH_SHORT).show();
                            displayListSubject();
                    }
                }
    })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create an alert dialog
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    /**
     * this method to verify user input for lecturer userID and name
     * @return
     */
    public boolean verify(){

        if(editTextName.getText().toString().equals("")|| editTextId.getText().toString().equals("")
                ||editTextName.getText().toString().equals("")|| editTextId.getText().toString().equals("")){

            // if all edittext empty
            if(editTextName.getText().toString().equals("")&& editTextId.getText().toString().equals("") &&
                    editTextName.getText().toString().equals("")&& editTextId.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "User ID and Name must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;

                // if name empty
            }else if(editTextName.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "Name must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;

                //if id empty
            }else if(editTextId.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "User ID must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else if(editTextName.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "Name must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;

                //if id empty
            }else if(editTextId.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "User ID must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            // if id more that 5 length character
        }else if(editTextId.getText().toString().length() > 5){
            Toast.makeText(RegisterActivity.this, "User ID must be at most 5 character",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

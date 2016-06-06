package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableLecturer;
import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableSubject;
import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Lecturer;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class CreateAttendanceActivity extends AppCompatActivity{

    private Toolbar toolBar;
    private Spinner spinner;
    private Button btnTakeAttendance;
    private TextView tvUserId, tvName;
    TableSubject tblSubject;
    TableLecturer tbllecturer = new TableLecturer(this);
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    ArrayList<String>  data = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        tvName = (TextView)findViewById(R.id.tvName);
        tvUserId = (TextView)findViewById(R.id.tvUserID);
        btnTakeAttendance = (Button)findViewById(R.id.buttonCreateAttendance);
        btnTakeAttendance.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
        spinner = (Spinner)findViewById(R.id.spinner);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        loadSpinnerData();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Lecturer lecturer = tbllecturer.getDetailLecturer();
                tvUserId.setText("User ID: "+lecturer.getUserId());
                tvName.setText("Login as: "+lecturer.getName());
            }
        };
        Thread thr = new Thread(run);
        thr.start();
    }

    private void loadSpinnerData(){
        TableSubject tblSubject = new TableSubject(getApplicationContext());
        List<String> labels = tblSubject.getAllSubject();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    // open hotspot
    public void createAttendance(View view){

        String subject = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        int selectedRadio = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton)findViewById(selectedRadio);
        String type = radioButton.getText().toString();

        Intent intent = new Intent(CreateAttendanceActivity.this, MainActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("subject",subject);
        startActivity(intent);
    }
}

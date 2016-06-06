package psm.com.adilla.macaddressclients;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import psm.com.adilla.macaddressclients.Database.TableStudent;

public class IPAddressActivity extends AppCompatActivity {

    private EditText editTextIPAddress;
    private TextView textViewMatricNo, textViewName, textViewIP, textViewMAC;
    TableStudent tblStudent = new TableStudent(this);
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipaddress);

        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        editTextIPAddress = (EditText)findViewById(R.id.editTextIPAddress);
        textViewMatricNo = (TextView)findViewById(R.id.textViewMatricNo);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewMAC = (TextView)findViewById(R.id.textViewMAC);
        textViewIP = (TextView)findViewById(R.id.textViewInfo);


        Runnable run = new Runnable() {

            @Override
            public void run() {
                Student student = tblStudent.getStudent();
                textViewMatricNo.setText(student.getMatricNo());
                textViewName.setText(student.getName());
                textViewMAC.setText(student.getMACAddress());
                textViewIP.setText(student.getIpAdd());

                IPAddressActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( IPAddressActivity.this,
                                "In this page, you are allow to update IP Address of lecturer",
                                Toast.LENGTH_LONG).show();;
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();
    }

    public void updateIpAddress(View view){
        // update sql for ipaddress

        Runnable run = new Runnable() {
            @Override
            public void run() {

                Student student = new Student();
                student.setIpAdd(editTextIPAddress.getText().toString());
                tblStudent.updateIpAddress(student);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewIP.setText(editTextIPAddress.getText().toString());
                        Intent gotoMain = new Intent(IPAddressActivity.this, MainActivity.class);
                        startActivity(gotoMain);
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();
        textViewIP.setText(editTextIPAddress.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_wifi:
                fnWifi(this.getCurrentFocus());
            case R.id.action_main:
                fnMain(this.getCurrentFocus());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fnMain(View view){
        Intent intent = new Intent(IPAddressActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void fnWifi(View view){
        Intent intent = new Intent(IPAddressActivity.this, WifiActivity.class);
        startActivity(intent);
    }*/
}

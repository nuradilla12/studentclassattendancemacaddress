package psm.com.adilla.macaddressclients;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import psm.com.adilla.macaddressclients.Database.TableStudent;

public class RegisterActivity extends AppCompatActivity {

    public WifiManager wifiManager;
    public WifiInfo wifiInfo;
    public String macAdd ;
    EditText editTextMatric, editTextName;
    String enteredMatric , enteredName;
    Button buttonSave;
    TextView txtIpAdd;
    Student student;
    Toolbar toolBar;
    TableStudent tblStudent = new TableStudent(this);
    public int ip;
    public static final int portNo = 8088;
    public static final String address = "192.168.43.1";
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        editTextMatric = (EditText)findViewById(R.id.editTextMatricNo);
        editTextName = (EditText)findViewById(R.id.editTextName);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        txtIpAdd = (TextView)findViewById(R.id.txtIPAdd);
        enteredMatric = editTextMatric.getText().toString();
        enteredName = editTextName.getText().toString();

        // declare the wifi manager
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        // get MAC address of phone client
        macAdd = wifiInfo.getMacAddress();

        insertIPAddress();

        if(tblStudent.init() == true){
            Intent gotoMain = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(gotoMain);
        }
    }

    public void insertIPAddress(){
        LayoutInflater layout = LayoutInflater.from(RegisterActivity.this);
        View promptView = layout.inflate(R.layout.input_dialog_ip, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        alertDialog.setView(promptView);
        final TextView info = (TextView)promptView.findViewById(R.id.textViewInfo);
        final EditText etIpAddress = (EditText)promptView.findViewById(R.id.etIp);

        InetAddress address = InetAddress.getLoopbackAddress();
        System.out.println("Name: " + address.getHostName());
        System.out.println("Addr: " + address.getHostAddress());
        System.out.println(address.isLoopbackAddress());

        //etIpAddress.setText();
        alertDialog.setMessage(" Welcome to Attendance Taker. Attendance Taker will be" +
                " performed well if student already connected to\nWi-Fi of your Lecturer. " +
                "Please insert lecturer's IP Address:");
        alertDialog.setTitle("Insert subject information:");

        alertDialog.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // save the ip address in the database sqlite

                        txtIpAdd.setText(etIpAddress.getText().toString());

                    }
                });
        // create an alert dialog
        AlertDialog alert = alertDialog.create();
        alert.show();

    }
    /**
     * This method to attend class which means transfer students details to lecturer phone. transfer
     * details of matric No, MAC Address, DateTime of attend that class.
     *
     * @param view
     */
    public void registerStudent(View view) {

        if(verify()) {
            Runnable run = new Runnable() {

                @Override
                public void run() {

                    MyClientTask myClientTask = new MyClientTask(address, portNo);
                    myClientTask.execute();
                }
            };
            Thread thr = new Thread(run);
            thr.start();
        }
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress = address;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

                Runnable run = new Runnable(){

                    public void run(){
                        Student student = new Student();
                        student.setMatricNo(editTextMatric.getText().toString());
                        student.setName(editTextName.getText().toString());
                        student.setMACAddress(macAdd);
                        student.setIpAdd(txtIpAdd.getText().toString());
                        tblStudent.addStudent(student);

                        System.out.println("new register : " + student.getMACAddress() +"|" +
                                student.getName() +":" + student.getMatricNo());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent gotoMain = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(gotoMain);
                                buttonSave.setEnabled(false);
                            }
                        });
                    }
                };
                Thread thr = new Thread(run);
                thr.run();

                try {
                    // initialize socket address
                    socket = new Socket(ipAddress(), dstPort);

                    // sending MAC Address to the server side
                    DataOutputStream outMAC = new DataOutputStream(socket.getOutputStream());
                    outMAC.writeUTF(macAdd);

                    // sending Matric number to server side
                    DataOutputStream outMat = new DataOutputStream(socket.getOutputStream());
                    outMat.writeUTF(matricNo());

                    // sending name attend the class to server side
                    DataOutputStream outName = new DataOutputStream(socket.getOutputStream());
                    outName.writeUTF(name());

                    ByteArrayOutputStream byteArrayOutputStream =
                            new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    System.out.println("Matric : " + matricNo());

                    InputStream inputStream = socket.getInputStream();

                    // inputStream.read() will block if no data return
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");
                    }
                } catch (UnknownHostException e) {

                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {

                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //txtViewMacAdd.setText(response);
            Toast toast = Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG);
            toast.show();
            super.onPostExecute(result);
            buttonSave.setEnabled(false);
            buttonSave.setBackgroundColor(Color.GRAY);
        }
    }

    public final String ipAddress(){

        String ipAddress = txtIpAdd.getText().toString();

        return ipAddress;
    }

    public final String matricNo() {

        String matricNo = editTextMatric.getText().toString();
        return matricNo;
    }

    public final String name() {
        String name = editTextName.getText().toString();
        return name;
    }

    public void nextPage(View view){

        Intent gotoMainActivity = new Intent(this, MainActivity.class);
        startActivity(gotoMainActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            /*case R.id.action_wifi:
                fnListWifi(this.getCurrentFocus());*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void fnListWifi(View view){
        Intent intent = new Intent(this, WifiActivity.class);
        startActivityForResult(intent, 0);
    }

    public boolean verify(){

        if(editTextName.getText().toString().equals("")|| editTextMatric.getText().toString().equals("")){
            if(editTextName.getText().toString().equals("")&& editTextMatric.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "Matric Number and Name must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else if(editTextName.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "Name must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else if(editTextMatric.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "Matric Number must be filled",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if(editTextMatric.getText().toString().length() > 10){
            Toast.makeText(RegisterActivity.this, "Matric Number must be at most 10 character",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

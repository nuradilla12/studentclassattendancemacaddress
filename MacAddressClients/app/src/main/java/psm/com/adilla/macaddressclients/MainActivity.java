package psm.com.adilla.macaddressclients;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import psm.com.adilla.macaddressclients.Database.TableStudent;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextMacAdd;
    private TextView textViewMatricNo, txtIP;
    private Button buttonAttend;
    public WifiManager wifiManager;
    public WifiInfo wifiInfo;
    public String macAdd;
    TableStudent tblStudent = new TableStudent(this);
    public int ip;
    Toolbar toolBar;
    public static final int portNo = 8088;
    public static final String address = ""; // server ip address
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        textViewMatricNo = (TextView) findViewById(R.id.textViewMatricNo);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMacAdd = (EditText) findViewById(R.id.editTextMACAdd);
        buttonAttend = (Button) findViewById(R.id.buttonAttend);
        txtIP = (TextView)findViewById(R.id.textViewIpAdd);

        // declare the wifi manager
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        // get MAC address of phone client
        macAdd = wifiInfo.getMacAddress();

        editTextMacAdd.setEnabled(false);
        textViewMatricNo.setEnabled(false);
        editTextName.setEnabled(false);

        Runnable run = new Runnable() {

            @Override
            public void run() {
                Student student = tblStudent.getStudent();
                editTextName.setText(student.getName());
                textViewMatricNo.setText(student.getMatricNo());
                editTextMacAdd.setText(student.getMACAddress());
                txtIP.setText(student.getIpAdd());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextMacAdd.setEnabled(false);
                        textViewMatricNo.setEnabled(false);
                        editTextName.setEnabled(false);
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();
    }

    /**
     * This method to attend class which means transfer students details to lecturer phone. trasnfer
     * details of matric No, MAC Address, DateTime of attend that class.
     *
     * @param view
     */
    public void attendClass(View view) {

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

            try {
                // initialize socket address
                socket = new Socket(ipAddress(), dstPort);

                // sending MAC Address to the server side
                DataOutputStream outMAC = new DataOutputStream(socket.getOutputStream());
                outMAC.writeUTF(macAdd);

                // sending Matric number to server side
                DataOutputStream outMat = new DataOutputStream(socket.getOutputStream());
                outMat.writeUTF(matricNo());
                System.out.println("Matric : " + matricNo());

                // sending name attend the class to server side
                DataOutputStream outName = new DataOutputStream(socket.getOutputStream());
                outName.writeUTF(name());

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;

                InputStream inputStream = socket.getInputStream();

                // notice:
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
            Toast toast = Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG);
            toast.show();
            super.onPostExecute(result);
            buttonAttend.setEnabled(false);
            buttonAttend.setBackgroundColor(Color.GRAY);
        }
    }

    public final String matricNo() {

        String matricNo = textViewMatricNo.getText().toString();
        return matricNo;
    }

    public final String name() {
        String name = editTextName.getText().toString();
        return name;
    }

    public final String dateTimeNow() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public final String ipAddress(){

        String ipAddress = txtIP.getText().toString();

        return ipAddress;
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
            case R.id.action_ipaddress:
                fnIpaddress(this.getCurrentFocus());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fnIpaddress(View view){
        Intent intent = new Intent(MainActivity.this, IPAddressActivity.class);
        startActivity(intent);
    }

    public void fnWifi(View view){
        Intent intent = new Intent(MainActivity.this, WifiActivity.class);
        startActivity(intent);
    }
}

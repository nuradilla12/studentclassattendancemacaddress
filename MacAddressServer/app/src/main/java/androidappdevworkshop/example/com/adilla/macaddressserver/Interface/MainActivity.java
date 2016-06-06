package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableAttendance;
import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableStudent;
import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Attendance;
import androidappdevworkshop.example.com.adilla.macaddressserver.Object.Student;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

/**
 * The list of registration of students
 */
public class MainActivity extends AppCompatActivity {

    TextView txtInfo, txtInfoIP, txtMsg, txtSubCode, txtSubType, txtDateTime, txtStatus;
    String message = "";
    Toolbar toolBar;
    ServerSocket serverSocket;
    String strMatric, strName, strMAC ;
    TableStudent tblStudent;
    TableAttendance tblAttendance = new TableAttendance(this);
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtInfo = (TextView)findViewById(R.id.info);
        txtInfoIP = (TextView)findViewById(R.id.infoip);
        txtMsg = (TextView)findViewById(R.id.msg);
        txtDateTime = (TextView)findViewById(R.id.tvDateTime);
        txtSubCode = (TextView)findViewById(R.id.tvSubCode);
        txtSubType = (TextView)findViewById(R.id.tvAttType);
        txtStatus = (TextView)findViewById(R.id.tvStatus);

        Intent intent = getIntent();
        String strSubject = intent.getStringExtra("subject");
        String strType = intent.getStringExtra("type");

        txtSubType.setText(strType);
        txtSubCode.setText(strSubject);
        txtDateTime.setText("(Date created:" +dateTimeNow()+" )");
        txtInfoIP.setText(getIpAddress());
        tblStudent =  new TableStudent(getApplicationContext());

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8088;
        int count = 0 , register = 0, attendNo = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                //txtInfo.setText(SocketServerPORT);

                while (true) {
                    // Socket that accept the attendance data
                    Socket socket = serverSocket.accept();
                    // getting MAC Address
                    DataInputStream inMAC = new DataInputStream(socket.getInputStream());
                    strMAC = inMAC.readUTF();

                    // getting matric number
                    DataInputStream inMat = new DataInputStream(socket.getInputStream());
                    strMatric = inMat.readUTF();

                    // getting name
                    DataInputStream inName = new DataInputStream(socket.getInputStream());
                    strName = inName.readUTF();

                    Student student = new Student();
                    // method verify register , if {} boolean true = student done register and set subject
                    if(tblStudent.verifyStudentRegistered(strMAC) == true){

                        // class attendance , insert attendance.
                        Attendance attend = new Attendance();
                        attend.setAttendance("Attend");
                        attend.setSubjectCode(txtSubCode.getText().toString());
                        attend.setType(txtSubType.getText().toString());
                        attend.setDateTime(dateTimeNow());
                        attend.setMatricNo(strMatric);
                        tblAttendance.attendClass(attend);

                        System.out.println("Attendance : " + strMatric + " |" + strName);
                        attendNo++;
                        count++;
                        message += "Attendance #" + attendNo + " from " + socket.getInetAddress()
                                + " : " + socket.getPort() + "\n" + strMAC.toString() +" | "
                                + strName + "\n";
                    }else{

                        // else student need to register
                        student.setMACAddress(strMAC);
                        student.setName(strName);
                        student.setMatricNo(strMatric);
                        tblStudent.addStudent(student);

                        register++;
                        count++;
                        System.out.println("Register for : " + strMAC + " & " +strMatric + " |" + strName);

                        message += "Registration @" + register + " from " + socket.getInetAddress()
                                + " : " + socket.getPort() + "\n" + strMAC.toString() +" | "
                                + strName + "\n";
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            txtMsg.setText(message);
                            txtStatus.setText(count + " processes that student connected with you..");
                        }
                    });
                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
                            socket, count);
                    socketServerReplyThread.run();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * message replay server
     */
    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "#" + cnt + " [" + strMatric + "]";

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n\n";

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        txtMsg.setText(message);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    txtMsg.setText(message);
                }
            });
        }
    }

    /**
     * this method to get IP address in this phone
     * @return
     */
    private String getIpAddress() {
        String ip = "";
        int intFlag = 0;
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements())
                {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        if(intFlag == 0)
                        {
                            ip += "IP Address: "
                              + inetAddress.getHostAddress() + "\n";
                            intFlag++;
                        }
                    }
                }
            }

        } catch (SocketException e) {

            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    /**
     * this method to get the datetime format
     * @return
     */
    public final String dateTimeNow() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW, Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    public void openAttendanceList(View view){
        Intent gotoAttendanceList= new Intent(MainActivity.this, AttendanceListActivity.class);
        startActivity(gotoAttendanceList);
    }

    public void openStudentList(View view){
        Intent gotoStudentList = new Intent(MainActivity.this, StudentListActivity.class );
        startActivity(gotoStudentList);
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
            case R.id.action_studentlist:
                fnStudentList(this.getCurrentFocus());
            case R.id.action_attendancelist:
                fnAttendance(this.getCurrentFocus());
            case R.id.action_insertsubject:
                fnInsertSubject(this.getCurrentFocus());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fnStudentList(View view){
        Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
        startActivity(intent);
    }
    public void fnAttendance(View view){
        Intent intent = new Intent(MainActivity.this, AttendanceListActivity.class);
        startActivity(intent);
    }

    public void fnInsertSubject(View view){
        Intent intent = new Intent(MainActivity.this, InsertSubjectActivity.class);
        startActivity(intent);
    }
}

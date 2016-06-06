package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import androidappdevworkshop.example.com.adilla.macaddressserver.Database.TableLecturer;
import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences shared;


    TableLecturer tblLecturer;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        tblLecturer = new TableLecturer(getApplicationContext());

        shared=getSharedPreferences("com.example", Context.MODE_PRIVATE);
        shared.edit().putBoolean("first_time",true).apply();

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if(shared.getBoolean("first_time",false)){
                    if(shared.getBoolean("first_time",false) && tblLecturer.getLecturer() == true){
                        startActivity(new Intent(SplashActivity.this, CreateAttendanceActivity.class));
                        finish();
                    } else{
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    }
                }else if(shared.getBoolean("first_time", true )){
                    if(shared.getBoolean("first_time",true) && tblLecturer.getLecturer() == false){
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    }
                }

                /*// checking whether lecturer is registered
                if(tblLecturer.getLecturer() == true){
                    startActivity(new Intent(SplashActivity.this, CreateAttendanceActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    finish();
                }*/

            }
        }, secondsDelayed * 1000);
    }
}

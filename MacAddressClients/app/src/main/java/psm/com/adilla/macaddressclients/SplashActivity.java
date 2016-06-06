package psm.com.adilla.macaddressclients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){

            public void run(){

                try{
                    sleep(1000);
                    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    if(!prefs.getBoolean("UserRegister", false)){

                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }/*catch (InterruptedException e){
                    e.printStackTrace();
                }*/
            }
        };
        thread.start();
    }
}

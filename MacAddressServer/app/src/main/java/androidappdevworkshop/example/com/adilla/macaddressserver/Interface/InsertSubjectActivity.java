package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class InsertSubjectActivity extends AppCompatActivity {

    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_subject);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

    }
}

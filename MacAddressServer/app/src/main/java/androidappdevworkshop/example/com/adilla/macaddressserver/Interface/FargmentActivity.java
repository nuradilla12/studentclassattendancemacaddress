package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import androidappdevworkshop.example.com.adilla.macaddressserver.R;

public class FargmentActivity extends AppCompatActivity {

    Toolbar toolBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fargment);
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPagerAdapter.addFragments(new AttendanceFragment(), "ATTENDANCE");
        viewPagerAdapter.addFragments(new HistoryFragment(), "HISTORY");
        viewPagerAdapter.addFragments(new BlankFragment(), "ABOUT ME");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    /*@Override
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
            case R.id.action_student:
                fnMain(this.getCurrentFocus());
            case R.id.action_attendance:
                //fnAttendance(this.getCurrentFocus());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fnMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
}

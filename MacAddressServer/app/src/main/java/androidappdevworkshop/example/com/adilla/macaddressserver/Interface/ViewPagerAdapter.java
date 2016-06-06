package androidappdevworkshop.example.com.adilla.macaddressserver.Interface;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Adilla on 29/5/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    int numOfTabs;

    /**
     * Constructor for ViewPagerAdapter
     * @param fm
     */
    public ViewPagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs =numOfTabs;
    }

    public void addFragments(Fragment fragments,String tabTitles){

        this.fragments.add(fragments);
        this.tabTitles.add(tabTitles);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles.get(position);
    }
}

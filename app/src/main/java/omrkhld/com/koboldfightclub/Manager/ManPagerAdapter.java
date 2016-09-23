package omrkhld.com.koboldfightclub.Manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Omar on 23/9/2016.
 */

public class ManPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private String tabTitles[] = new String[] {"Encounter Manager", "Player Manager"};

    public ManPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EncManagerFragment.newInstance();
            case 1:
                return PCManagerFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

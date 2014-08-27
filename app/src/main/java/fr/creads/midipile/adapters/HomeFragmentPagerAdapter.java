package fr.creads.midipile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    static final int NUM_ITEMS = 2;

    public HomeFragmentPagerAdapter(FragmentActivity activity, List<Fragment> fragments) {
        super(activity.getSupportFragmentManager());
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}

package fr.creads.midipile.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.adapters.HomeFragmentPagerAdapter;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class HomeFragment extends Fragment implements TabHost.OnTabChangeListener  {

    private FragmentActivity myContext;
    private ActionBar actionBar;
    private TabHost tabHost;

    private HomeFragmentPagerAdapter mAdapter;
    private ViewPager mPager;

    private String[] mTabsTitles;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        actionBar = myContext.getActionBar();
        Log.d("fr.creads.midipile", "homefragment create ======");

        mTabsTitles = getResources().getStringArray(R.array.home_fragment_tabs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("fr.creads.midipile", "homefragment create view  ======");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupAdapter(rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        //((HomeActivity) activity).onSectionAttached(getArguments().getInt("1"));
    }

    private void setupAdapter(View rootView){

        // Initialization
        tabHost=(TabHost)rootView.findViewById(R.id.home_tab_host);
        tabHost.setup();


        for (String tab_name : mTabsTitles) {
            TabHost.TabSpec spec=tabHost.newTabSpec(tab_name);
            spec.setContent(R.id.fakeTabContent);
            spec.setIndicator(tab_name);
            tabHost.addTab(spec);
        }

        tabHost.setOnTabChangedListener(this);

        mAdapter = new HomeFragmentPagerAdapter(myContext, createFragments());

        mPager = (ViewPager)rootView.findViewById(R.id.homepager);
        mPager.setAdapter(mAdapter);


        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        mPager.setCurrentItem(position);
                        tabHost.setCurrentTab(position);
                    }
                }
        );
    }

    private List<Fragment> createFragments() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(Fragment.instantiate(myContext, DealsDayFragment.class.getName()));
        list.add(Fragment.instantiate(myContext, WhishlistFragment.class.getName()));

        return list;
    }

    @Override
    public void onTabChanged(String tab) {
        Log.d("fr.creads.midipile", "homefragment onTabChanged  ======" + tab);

        if(tab.equals(mTabsTitles[0])){
            mPager.setCurrentItem(0);
            Log.d("fr.creads.midipile", "homefragment setCurrentItem  ======");
        } else {
            mPager.setCurrentItem(1);
        }
    }
}

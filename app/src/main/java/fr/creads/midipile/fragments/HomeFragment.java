package fr.creads.midipile.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.adapters.HomeFragmentPagerAdapter;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class HomeFragment extends Fragment implements ActionBar.TabListener  {

    private FragmentActivity myContext;
    private ActionBar actionBar;

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
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mAdapter = new HomeFragmentPagerAdapter(myContext, createFragments());

        for (String tab_name : mTabsTitles) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        mPager = (ViewPager)rootView.findViewById(R.id.homepager);
        mPager.setAdapter(mAdapter);


        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        Log.d("fr.creads.midipile", "homefragment ontabSelected  ======" + tab.getPosition());
        if(mPager != null){

            mPager.setCurrentItem(tab.getPosition());

            Log.d("fr.creads.midipile", "homefragment setCurrentItem  ======");
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}

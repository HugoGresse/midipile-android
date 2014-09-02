package fr.creads.midipile.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.HomeFragmentPagerAdapter;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class HomeFragment extends Fragment
        implements TabHost.OnTabChangeListener, OnDataLoadedListener {

    private HomeActivity homeActivity;
    private FragmentActivity myContext;
    private ActionBar actionBar;
    private onHomeFragmentSelectedListener mCallback;
    private TabHost tabHost;

    private HomeFragmentPagerAdapter mAdapter;
    private ViewPager mPager;

    private String[] mTabsTitles;

    @Override
    public void onDealsLoaded() {
        setDealsAdapter();
    }

    public interface onHomeFragmentSelectedListener {
        public void onDealsSelected(int dealId);
    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        actionBar = myContext.getActionBar();

        mTabsTitles = getResources().getStringArray(R.array.home_fragment_tabs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupAdapter(rootView);
        Log.d("fr.creads.midipile", "CREATE HomeFRAGMENT");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        homeActivity = (HomeActivity) activity;

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onHomeFragmentSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }


    private void setupAdapter(View rootView){

        // Initialization
        tabHost=(TabHost)rootView.findViewById(R.id.home_tab_host);
        tabHost.setup();

        for (String tab_name : mTabsTitles) {

            View tabview = createTabView(tabHost.getContext(), tab_name);

            TabHost.TabSpec spec=tabHost.newTabSpec(tab_name);
            spec.setContent(new TabHost.TabContentFactory() {
                public View createTabContent(String tag) {
                    return new TextView(myContext);
                }
            });
            spec.setIndicator(tabview);
            tabHost.addTab(spec);
        }

        tabHost.setOnTabChangedListener(this);

        mAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager(), createFragments());

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

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
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

    public void setDealsAdapter(){
        DealsDayFragment dealsDayFragment = (DealsDayFragment) mAdapter.getItem(0);

        ArrayList<Deal> deals =  homeActivity.getLastDeals();
        Log.i("midipile ", "setDealsAdapter");

        if( !deals.isEmpty()) {
            dealsDayFragment.setDealsAdapters(homeActivity.getLastDeals());
        }

    }
}

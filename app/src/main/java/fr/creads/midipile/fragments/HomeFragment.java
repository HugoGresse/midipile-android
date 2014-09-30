package fr.creads.midipile.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.TabFragmentPagerAdapter;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.listeners.OnDealsLoadedListener;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class HomeFragment extends Fragment
        implements TabHost.OnTabChangeListener, OnDealsLoadedListener, OnDataLoadedListener {

    private static final String SCREEN_NAME1 = "Offres du jour";
    private static final String SCREEN_NAME2 = "Whishlist";

    private HomeActivity homeActivity;
    private FragmentActivity myContext;
    private ActionBar actionBar;
    private TabHost tabHost;

    private TabFragmentPagerAdapter mAdapter;
    private ViewPager mPager;

    private String[] mTabsTitles;

    @Override
    public void onDealsLoaded() {
        setDealsAdapter();
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

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.fragmentHomeLayout);
        setInsets(this.getActivity(), layout);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        homeActivity = (HomeActivity) activity;
    }

    @Override
    public void onResume (){
        super.onResume();
    }

    @Override
    public void onDataLoaded() {
        ((WishlistFragment) mAdapter.getRegisteredFragment(1)).setAdapters(
                (ArrayList<Deal>) ((HomeActivity) getActivity()).getWhishlist()
        );
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

        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), createFragments());

        mPager = (ViewPager)rootView.findViewById(R.id.homepager);
        mPager.setAdapter(mAdapter);

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        mPager.setCurrentItem(position);
                        tabHost.setCurrentTab(position);

                        if(position == 0){
                            ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME1);
                        } else {
                            ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME2);
                        }
                    }
                }
        );

        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME1);

    }

    private List<Fragment> createFragments() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(Fragment.instantiate(myContext, DealsDayFragment.class.getName()));
        list.add(Fragment.instantiate(myContext, WishlistFragment.class.getName()));

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
        if(tab.equals(mTabsTitles[0])){
            mPager.setCurrentItem(0);
        } else {
            mPager.setCurrentItem(1);
        }
    }

    public void setDealsAdapter(){
        if(null != mAdapter) {
            DealsDayFragment dealsDayFragment = (DealsDayFragment) mAdapter.getItem(0);
            ArrayList<Deal> deals =  homeActivity.getLastDeals();
            if( !deals.isEmpty()) {
                dealsDayFragment.setDealsAdapters(homeActivity.getLastDeals());
            }
        }

    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true) , config.getPixelInsetRight(), 0);
    }

    public void setPosition(int pos){
        if(null != tabHost && null != mPager){
            tabHost.setCurrentTab(pos);
            mPager.setCurrentItem(pos);
        }
    }

}

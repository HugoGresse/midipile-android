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

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.TabFragmentPagerAdapter;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealFragment extends Fragment
        implements TabHost.OnTabChangeListener, OnDataLoadedListener {

    ActionBar actionBar;
    FragmentActivity myContext;
    HomeActivity homeActivity;

    private TabHost tabHost;
    private TabFragmentPagerAdapter mAdapter;
    private ViewPager mPager;
    private String[] mTabsTitles;

    private Deal deal;

    @Override
    public void onDealsLoaded() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        actionBar = myContext.getActionBar();

        mTabsTitles = getResources().getStringArray(R.array.deal_fragment_tabs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deal, container, false);
        setupAdapter(rootView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.fragmentDealLayout);
        setInsets(this.getActivity(), layout);

        deal = getArguments().getParcelable("deal");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        homeActivity = (HomeActivity) activity;
    }

    private void setupAdapter(View rootView){

        // Initialization
        tabHost=(TabHost)rootView.findViewById(R.id.deal_tab_host);
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

        mPager = (ViewPager)rootView.findViewById(R.id.dealpager);
        mPager.setAdapter(mAdapter);

        // set currentItem to product
        mPager.setCurrentItem(1);
        tabHost.setCurrentTab(1);

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
        list.add(Fragment.instantiate(myContext, DealBrandFragment.class.getName()));
        list.add(Fragment.instantiate(myContext, DealProductFragment.class.getName()));
        list.add(Fragment.instantiate(myContext, DealPlaceFragment.class.getName()));

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
        } else if(tab.equals(mTabsTitles[1])){
            mPager.setCurrentItem(1);
        } else {
            mPager.setCurrentItem(2);
        }
    }


    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true) , config.getPixelInsetRight(), 0);
    }

}

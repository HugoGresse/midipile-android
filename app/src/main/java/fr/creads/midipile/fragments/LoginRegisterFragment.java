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
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.TabFragmentPagerAdapter;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class LoginRegisterFragment extends Fragment implements TabHost.OnTabChangeListener {

    private static final String SCREEN_NAME_LOGIN = "Login";
    private static final String SCREEN_NAME_REGISTER = "Register";

    private ActionBar actionBar;
    private ShareActionProvider mShareActionProvider;

    private FragmentActivity myContext;
    private HomeActivity homeActivity;

    private TabHost tabHost;
    private TabFragmentPagerAdapter mAdapter;
    private ViewPager mPager;
    private String[] mTabsTitles;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        actionBar = myContext.getActionBar();
        this.setHasOptionsMenu(true);

        mTabsTitles = getResources().getStringArray(R.array.user_loginregister_tabs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_loginregister, container, false);
        setupAdapter(rootView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.fragmentloginRegisterLayout);
        setInsets(this.getActivity(), layout);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        homeActivity = (HomeActivity) activity;
    }

    private void setupAdapter(View rootView){

        // Initialization
        tabHost=(TabHost)rootView.findViewById(R.id.loginregister_tab_host);
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

        mPager = (ViewPager)rootView.findViewById(R.id.loginregisterpager);
        mPager.setAdapter(mAdapter);

        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME_LOGIN);

        // set currentItem
        // mPager.setCurrentItem(1);
        //tabHost.setCurrentTab(1);

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        mPager.setCurrentItem(position);
                        tabHost.setCurrentTab(position);

                        if(position == 0){
                            ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME_LOGIN);
                        } else {
                            ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME_REGISTER);
                        }
                    }
                }
        );

    }

    private List<Fragment> createFragments() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(Fragment.instantiate(myContext, LoginRegisterLoginFragment.class.getName()));
        list.add(Fragment.instantiate(myContext, LoginRegisterRegisterFragment.class.getName()));

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


    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true) , config.getPixelInsetRight(), 0);
    }
}

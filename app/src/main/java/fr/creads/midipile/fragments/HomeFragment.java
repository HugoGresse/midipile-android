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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.adapters.HomeFragmentPagerAdapter;
import fr.creads.midipile.api.MidipileRestClient;
import fr.creads.midipile.objects.Deal;

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

        mTabsTitles = getResources().getStringArray(R.array.home_fragment_tabs);
        try {
            loadDeals();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void loadDeals() throws JSONException {

        Log.d("fr.creads.midipile", "load deals  ======");

        MidipileRestClient.get("/deals/500", null, new  BaseJsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.d("fr.creads.midipile", "===== LOAD DONE");

                Gson gson = new GsonBuilder().create();
                Deal deal = gson.fromJson(rawJsonResponse, Deal.class);

                actionBar.setTitle(deal.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }

            /*@Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("fr.creads.midipile", String.valueOf(responseBody));

                Gson gson = new Gson();
                Deal deal = gson.fromJson(String.valueOf(responseBody), Deal.class);

                actionBar.setTitle(deal.getName());
            }*/

        });
    }
}

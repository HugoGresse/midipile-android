package fr.creads.midipile.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fr.creads.midipile.dialogs.DealNetworkDialogFragment;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.R;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.api.MidipileAPI;
import fr.creads.midipile.fragments.HomeFragment;
import fr.creads.midipile.fragments.LastWinnerFragment;
import fr.creads.midipile.navigationdrawer.NavigationDrawerFragment;
import fr.creads.midipile.objects.Deal;
import fr.creads.midipile.objects.Deals;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, HomeFragment.onHomeFragmentSelectedListener {

    private MidipileAPI midipileService;

    private ArrayList<Deal> deals;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private OnDataLoadedListener mLoadedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        deals = new ArrayList<Deal>();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.URL_API)
                .build();

        midipileService = restAdapter.create(MidipileAPI.class);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        loadLastDeals();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d("fr.creads.midipile", "onNavigationDrawerItemSelected ====== position:" + Integer.toString(position));
        switch (position) {
            case 0:
                changeFragment( new HomeFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 1:
                changeFragment( new LastWinnerFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 2:
                changeFragment( new LastWinnerFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                changeFragment( new HomeFragment(), number,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    /**
     * Change content fragment
     * @param frag
     * @param position
     */
    private void changeFragment(Fragment frag, int position, int animIn, int animOut){

        Log.d("fr.creads.midipile", "Change fragment ======");
        // animation : .setCustomAnimations(animIn, animOut, animIn, animOut)

        // Fragment must implement the callback.
        if (!(frag instanceof OnDataLoadedListener)) {
            throw new IllegalStateException(
                    "Fragment must implement the onDataLoadedListener.");
        }
        mLoadedCallbacks = (OnDataLoadedListener) frag;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()

            .replace(R.id.container, frag, Integer.toString(position))
            .addToBackStack(frag.getClass().toString())
            .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadLastDeals(){

        midipileService.getLastDeals(new Callback<Deals>() {
            @Override
            public void success(Deals d, Response response) {
                deals = (ArrayList<Deal>) d.getDeals();
                Log.i("fr.creads.midipile", "Deals loaded");
                mLoadedCallbacks.onDealsLoaded();
            }

            @Override
            public void failure(RetrofitError error) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Create and show the dialog.
                DealNetworkDialogFragment dealNetworkDialogFragment = new DealNetworkDialogFragment();
                dealNetworkDialogFragment.show(ft, "dialog");
                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    public ArrayList<Deal> getLastDeals(){
        if(deals.isEmpty()){
            Log.i("fr.creads.midipile", "getDealsEmpty");
            return new ArrayList<Deal>();
        } else {
            Log.i("fr.creads.midipile", "gettingDeals");
            return deals;
        }
    }


    @Override
    public void onDealsSelected(int dealId) {

    }
}

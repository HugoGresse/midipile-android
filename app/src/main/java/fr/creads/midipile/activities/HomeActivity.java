package fr.creads.midipile.activities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

import fr.creads.midipile.R;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.api.MidipileAPI;
import fr.creads.midipile.dialogs.DealNetworkDialogFragment;
import fr.creads.midipile.fragments.DealFragment;
import fr.creads.midipile.fragments.DealProductFragment;
import fr.creads.midipile.fragments.DealsDayFragment;
import fr.creads.midipile.fragments.HomeFragment;
import fr.creads.midipile.fragments.LastWinnerFragment;
import fr.creads.midipile.fragments.LoginRegisterFragment;
import fr.creads.midipile.fragments.LoginRegisterLoginFragment;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.navigationdrawer.NavigationDrawerFragment;
import fr.creads.midipile.objects.Deal;
import fr.creads.midipile.objects.Deals;
import fr.creads.midipile.objects.User;
import fr.creads.midipile.utilities.MidipileUtilities;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class HomeActivity extends FragmentActivity
        implements
            NavigationDrawerFragment.NavigationDrawerCallbacks,
            DealsDayFragment.onDealsSelectedListener,
            DealProductFragment.onButtonParticipateClickListener,
            LoginRegisterLoginFragment.onButtonClickListener {

    private static final String USER_SHAREDPREF = "userlogged";

    private MidipileAPI midipileService;


    private ArrayList<Deal> deals;
    private int dealPosition;

    /**
     * The user connnected to the app
     */
    private User user;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private OnDataLoadedListener mLoadedCallbacks;

    private boolean homeFragmentAlreadyCreated = false;

    private Typeface latoTypeface;
    private Typeface latoBoldTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide ActionBar on start
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        super.onCreate(savedInstanceState);


        latoTypeface = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");
        latoBoldTypeface = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        // init rest api
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.URL_API)
                .setConverter(new GsonConverter(gson))
                .build();
        midipileService = restAdapter.create(MidipileAPI.class);



        setContentView(R.layout.fragment_splashscren);

        enableSystemBarTint();

        loadLastDeals();

        user = getUser();
    }

    /**
     * Init the acvitity and navigatio ndrawer after spalsh screen
     */
    public void afterOnCreate(){
        if(homeFragmentAlreadyCreated) return;

        getActionBar().show();

        setContentView(R.layout.activity_home);

        deals = new ArrayList<Deal>();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        enableSystemBarTint();
        homeFragmentAlreadyCreated = true;

        // display user in nav drawer
        if(null != user){
            Log.d(Constants.TAG, "display user");
            mNavigationDrawerFragment.displayUser(user);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d("fr.creads.midipile", "onNavigationDrawerItemSelected ====== position:" + Integer.toString(position));

        if(null != user) {
            position -= 1;
        }

        switch (position) {
            case 0:
                changeFragment( new HomeFragment(), position);
                break;
            case 1:
                changeFragment( new LoginRegisterFragment(), position);
                break;
            case 2:
                changeFragment( new LastWinnerFragment(), position);
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
                changeFragment( new HomeFragment(), number);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }


    private void changeFragment(Fragment frag, int position){
        changeFragment(frag, position, R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Change content fragment
     * @param frag
     * @param position
     */
    private void changeFragment(Fragment frag, int position, int enter, int exit, int pop_enter, int pop_exit){
        // Fragment must implement the callback.
        if (!(frag instanceof OnDataLoadedListener)) {
            Log.e(Constants.TAG, "Fragment must implement the onDataLoadedListener.");
            // throw new IllegalStateException(
            // "Fragment must implement the onDataLoadedListener.");
        } else {
            mLoadedCallbacks = (OnDataLoadedListener) frag;
        }

        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(enter, exit, pop_enter, pop_exit);

            transaction.replace(R.id.container, frag, Integer.toString(position));
            transaction.addToBackStack(null);
            transaction.commit();
        } catch(IllegalStateException exception){
            Log.e(Constants.TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
        actionBar.setDisplayShowTitleEnabled(false);

    }

    public void enableSystemBarTint(){
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.background_actionBar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (null != mNavigationDrawerFragment && !mNavigationDrawerFragment.isDrawerOpen()) {
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

                if(null == mNavigationDrawerFragment){
                    afterOnCreate();
                }

                deals = (ArrayList<Deal>) d.getDeals();
                Log.i("fr.creads.midipile", "Deals loaded");

                if(null != mLoadedCallbacks) {
                    mLoadedCallbacks.onDealsLoaded();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                afterOnCreate();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Create and show the dialog.
                DealNetworkDialogFragment dealNetworkDialogFragment = new DealNetworkDialogFragment();
                dealNetworkDialogFragment.show(ft, "dialog");
                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    public ArrayList<Deal> getLastDeals(){
        if(null == deals || deals.isEmpty()){
            Log.i("fr.creads.midipile", "getDealsEmpty");
            return new ArrayList<Deal>();
        } else {
            Log.i("fr.creads.midipile", "gettingDeals");
            return deals;
        }
    }

    public Deal getDeal(int position){
        if(deals.isEmpty()){
            return new Deal();
        } else {
            return deals.get(position);
        }
    }


    public Deal getSelectedDeal(){
        if(deals.isEmpty()){
            return new Deal();
        } else {
            return deals.get(dealPosition);
        }
    }

    @Override
    public void onDealsSelected(int position) {
        dealPosition = position;

        Bundle args=new Bundle();
        args.putParcelable("deal", deals.get(position));

        Fragment dealFragment = new DealFragment();
        dealFragment.setArguments(args);

        changeFragment(dealFragment, 1, R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    }

    @Override
    public void onLoginClick(String email, String password) {

        // encrypt password before sending
        password = MidipileUtilities.getSha1(password);

        midipileService.postLogin(email, password, new Callback<User>() {
            @Override
            public void success(User u, Response response) {

                Toast.makeText(getApplicationContext(), "Vous êtes connecté", Toast.LENGTH_LONG).show();

                setSharedUser(u);
                mNavigationDrawerFragment.displayUser(u);

                Log.i(Constants.TAG, getUser().toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Impossible de vous connecter", Toast.LENGTH_SHORT).show();
                Log.i("fr.creads.midipile", error.toString());
                Log.i("fr.creads.midipile", error.getResponse().toString());
            }
        });
    }


    @Override
    public void onParticipateClick(Deal deal) {

        if(null != user){
            // user logged // send particpation
            Toast.makeText(getApplicationContext(), "Participation en cours", Toast.LENGTH_SHORT).show();

        } else {
            // loginRegister fragment set on position 8
            changeFragment( new LoginRegisterFragment(), 8);
        }
    }


    public Typeface getLatoTypeface(){
        return latoTypeface;
    }
    public Typeface getLatoBoldTypeface(){
        return latoBoldTypeface;
    }

    public void setSharedUser(User user){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp.edit().putString(USER_SHAREDPREF, new Gson().toJson(user)).apply();
    }

    public User getUser(){
        if(null == user) {
            // try to get user from shared pref

            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            String userString = sp.getString(USER_SHAREDPREF, null);

            if(null == userString || userString.isEmpty()) {
                return null;
            }

            Type type = new TypeToken<User>() {}.getType();
            user = (User) new Gson().fromJson(userString, type);
        }

        Log.d(Constants.TAG, Integer.toString(user.getChance()));

        return user;

    }

}

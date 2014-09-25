package fr.creads.midipile.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.api.MidipileAPI;
import fr.creads.midipile.broadcastreceiver.ParticipateNotificationBroadcastReceiver;
import fr.creads.midipile.fragments.DealFragment;
import fr.creads.midipile.fragments.DealProductFragment;
import fr.creads.midipile.fragments.DealsDayFragment;
import fr.creads.midipile.fragments.HomeFragment;
import fr.creads.midipile.fragments.LoginRegisterFragment;
import fr.creads.midipile.fragments.LoginRegisterLoginFragment;
import fr.creads.midipile.fragments.LoginRegisterRegisterFragment;
import fr.creads.midipile.fragments.UserFragment;
import fr.creads.midipile.fragments.UserProfilFragment;
import fr.creads.midipile.fragments.WinnersFragment;
import fr.creads.midipile.listeners.OnBadgesLoadedListener;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.listeners.OnDealsLoadedListener;
import fr.creads.midipile.navigationdrawer.NavigationDrawerFragment;
import fr.creads.midipile.objects.Badge;
import fr.creads.midipile.objects.Deal;
import fr.creads.midipile.objects.Deals;
import fr.creads.midipile.objects.User;
import fr.creads.midipile.utilities.MidipileUtilities;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

public class HomeActivity extends FragmentActivity
        implements
            NavigationDrawerFragment.NavigationDrawerCallbacks,
            DealsDayFragment.onDealsSelectedListener,
            DealProductFragment.onButtonParticipateClickListener,
            LoginRegisterLoginFragment.onButtonClickListener,
            LoginRegisterRegisterFragment.onRegisterButtonClickListener,
            UserProfilFragment.OnUserUpdateListener{

    private static final String USER_SHAREDPREF = "userlogged";
    private static final int ONEDAY_NOTIFICATION_ALARM = 1;
    private static final int FIVEDAY_NOTIFICATION_ALARM = 2;

    private MidipileAPI midipileService;

    private ArrayList<Deal> deals;
    private int dealPosition;

    private List<Badge> badges;

    /**
     * The user connnected to the app
     */
    private User user;

    /**
     * Deals user participated to
     */
    private List<Deal> whishlist;

    /**
     * Deals ended with winners
     */
    private List<Deal> winnersDeals;

    private SimpleFacebook mSimpleFacebook;

    private ProgressDialog mProgressDialog;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private OnDealsLoadedListener mLoadedCallbacks;
    private OnBadgesLoadedListener mBadgesLoadedCallbacks;

    private boolean homeFragmentAlreadyCreated = false;

    private Typeface latoTypeface;
    private Typeface latoBoldTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide ActionBar on start
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();


        mSimpleFacebook = SimpleFacebook.getInstance(this);

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

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                // click on user profil
                changeFragment( new UserFragment(), position);
                break;
            case 1:
                Bundle args=new Bundle();
                args.putInt("whishlist", 0);
                Fragment homeFrag = new HomeFragment();
                homeFrag.setArguments(args);
                changeFragment(homeFrag, position);
                break;
            case 2:
                Bundle args2=new Bundle();
                args2.putInt("whishlist", 1);
                Fragment homeFrag2 = new HomeFragment();
                homeFrag2.setArguments(args2);
                changeFragment(homeFrag2, position);
                break;
            case 3:
                changeFragment( new WinnersFragment(), position);
                break;
            case 4:
                break;
        }
    }


    private void changeFragment(Fragment frag, int position){
        changeFragment(frag, position, R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Init the acvitity and navigatio ndrawer after spalsh screen
     */
    public void afterOnCreate(){
        if(homeFragmentAlreadyCreated) return;

        getActionBar().show();
        getActionBar().setTitle(null);

        setContentView(R.layout.activity_home);

        deals = new ArrayList<Deal>();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        enableSystemBarTint();
        homeFragmentAlreadyCreated = true;

        // display user in nav drawer
        if(null != user){
            mNavigationDrawerFragment.displayUser(user);
        }

    }

    /**
     * Change content fragment
     * @param frag
     * @param position
     */
    private void changeFragment(Fragment frag, int position, int enter, int exit, int pop_enter, int pop_exit){
        changeFragment(frag, position, enter, exit, pop_enter, pop_exit, true);
    }

    /**
     * Change content fragment
     * @param frag
     * @param position
     */
    private void changeFragment(Fragment frag, int position, int enter, int exit, int pop_enter, int pop_exit, boolean addToBackStack){
        // Fragment must implement the callback.
        if (!(frag instanceof OnDealsLoadedListener)) {
            Log.e(Constants.TAG, "Fragment must implement the onDataLoadedListener.");
            // throw new IllegalStateException(
            // "Fragment must implement the onDataLoadedListener.");
        } else {
            mLoadedCallbacks = (OnDealsLoadedListener) frag;
        }

        String backStateName =  frag.getClass().getName();
        String fragmentTag = backStateName;


        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(enter, exit, pop_enter, pop_exit);
                transaction.replace(R.id.container, frag, frag.getClass().getName());
                transaction.addToBackStack(backStateName);
                transaction.commit();
                Log.d(Constants.TAG, "addToBackTack");
            } else {
                Log.d(Constants.TAG, "nothing to do");
                // set Whishlist tab if fragment is show
                if(frag instanceof HomeFragment && frag.getArguments() != null){
                    HomeFragment tempFrag = (HomeFragment) manager.findFragmentByTag(fragmentTag);
                    tempFrag.setPosition(frag.getArguments().getInt("whishlist"));
                }
            }
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

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments <= 1) {
            // kill app since cannont fo anymore back
            finish();
        } else {
            super.onBackPressed();
        }
    }

    protected void showDialog() {
        if (mProgressDialog == null) {
            setProgressDialog("Chargement des données");
        }
        mProgressDialog.show();
    }
    protected void showDialog(String title) {
        if (mProgressDialog == null) {
            setProgressDialog(title);
        }
        mProgressDialog.show();
    }
    protected void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void setProgressDialog(String title) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage("Chargement...");
    }


    public Typeface getLatoTypeface(){
        return latoTypeface;
    }
    public Typeface getLatoBoldTypeface(){
        return latoBoldTypeface;
    }









    public void loadLastDeals(){

        midipileService.getLastDeals(new Callback<Deals>() {
            @Override
            public void success(Deals d, Response response) {

                if(null == mNavigationDrawerFragment){
                    afterOnCreate();

                    if( null != user){
                        refreshUser();
                        loadWhishList();
                    }

                }

                deals = (ArrayList<Deal>) d.getDeals();

                if(null != mLoadedCallbacks) {
                    mLoadedCallbacks.onDealsLoaded();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                afterOnCreate();

                if(error.isNetworkError()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_network_error_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadLastDeals();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else {
                    SuperActivityToast successSuperToast = new SuperActivityToast(HomeActivity.this);
                    successSuperToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                    successSuperToast.setText( getResources().getString(R.string.toast_server_error) );
                    successSuperToast.show();
                }

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
            return null;
        } else {
            return deals.get(dealPosition);
        }
    }

    @Override
    public void onDealsSelected(int position) {
        dealPosition = position;

        onDealsSelected(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    }

    public void onDealsSelected(int animEnter, int animExit, int popEnter, int popExit) {
        Bundle args=new Bundle();
        args.putParcelable("deal", getSelectedDeal());

        Fragment dealFragment = new DealFragment();
        dealFragment.setArguments(args);

        changeFragment(dealFragment, 1, animEnter, animExit, popEnter, popExit, false);
    }









    @Override
    public void onLoginClick(String email, String password) {
        // encrypt password before sending
        password = MidipileUtilities.getSha1(password);

        showDialog("Connexion à Midipile");

        midipileService.postLogin(email, password, MidipileUtilities.getUniquePsuedoID(), new Callback<User>() {
            @Override
            public void success(User u, Response response) {
                hideDialog();

                // send login tracking event
                ((MidipileApplication)getApplication()).sendEventTracking(
                        R.string.tracker_user_category,
                        R.string.tracker_user_action_login,
                        Integer.toString(u.getId()));

                setUser(u, response.getHeaders());
            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                AlertDialog alertDialog = alertDialogBuilder
                        .setTitle(R.string.dialog_user_title)
                        .setMessage(R.string.dialog_user_text)
                        .setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .create();

                alertDialog.show();

                Log.i("fr.creads.midipile", error.toString());
                Log.i("fr.creads.midipile", error.getResponse().toString());
            }
        });
    }

    public String getResponseXwsseHeaders(List<Header> headers){
        for (Header header : headers) {

            if(null == header ){
                continue;
            }
            if(null == header.getName()){
                continue;
            }
            if(header.getName().isEmpty()){
                continue;
            }

            if( null != header.getValue() && !header.getValue().isEmpty() ){
                // add xwsse header to user
                if(  header.getName().equals("X-Wsse") ||  header.getName().equals("x-wsse")){
                    return header.getValue();
                }
            }
        }
        return "";
    }

    @Override
    public void sendFacebookLoginClick() {
        doFacebookLogin();
    }

    @Override
    public void onRegisterFacebook() {
        doFacebookLogin();
    }

    public void doFacebookLogin(){
        // if user is already logged
        if( !mSimpleFacebook.isLogin()){
            mSimpleFacebook.login(new OnLoginListener() {
                @Override
                public void onLogin() {
                    getFacebookProfile();
                }

                @Override
                public void onNotAcceptingPermissions(Permission.Type type) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_facebook_title)
                            .setMessage(R.string.dialog_facebook_nopermission)
                            .setPositiveButton(R.string.dialog_network_error_ok,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    doFacebookLogin();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                }

                @Override
                public void onThinking() {
                }

                @Override
                public void onException(Throwable throwable) {

                    hideDialog();
                }

                @Override
                public void onFail(String s) {

                    hideDialog();
                }
            });
        } else {
            getFacebookProfile();
        }
    }

    private void getFacebookProfile(){

        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.ID)
                .add(Profile.Properties.FIRST_NAME)
                .add(Profile.Properties.LAST_NAME)
                .add(Profile.Properties.EMAIL)
                .build();

        mSimpleFacebook.getProfile(new OnProfileListener() {
            @Override
            public void onThinking() {
                showDialog("Connexion à Facebook");
            }
            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                Log.d(Constants.TAG, throwable.getMessage());
            }
            @Override
            public void onFail(String reason) {
                hideDialog();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                AlertDialog alertDialog = alertDialogBuilder
                        .setTitle(R.string.dialog_facebook_title)
                        .setMessage(R.string.dialog_facebook_unabletogetprofil)
                        .setPositiveButton(R.string.dialog_network_error_ok,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                doFacebookLogin();
                            }
                        })
                        .setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .create();

                alertDialog.show();

            }
            @Override
            public void onComplete(Profile response) {
                postFacebookLoginRegister(
                        response.getId(),
                        response.getEmail(),
                        response.getFirstName(),
                        response.getLastName());

            }
        });
    }

    /**
     * Logged or register with facebook
     * @param email
     * @param fid
     * @param firstname
     * @param lastname
     */
    private void postFacebookLoginRegister(String fid, String email, String firstname, String lastname){

        midipileService.postLoginFacebook(email, fid, fid, firstname, lastname, "1", "android", MidipileUtilities.getUniquePsuedoID(), new Callback<User>() {
            @Override
            public void success(User u, Response response) {

                // send login/register fb tracking event
                ((MidipileApplication)getApplication()).sendEventTracking(
                        R.string.tracker_user_category,
                        R.string.tracker_user_action_login_facebook,
                        Integer.toString(u.getId()));

                setUser(u, response.getHeaders());
                hideDialog();
            }

            @Override
            public void failure(RetrofitError error) {

                hideDialog();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                AlertDialog alertDialog = alertDialogBuilder
                        .setTitle(R.string.dialog_user_title)
                        .setMessage(R.string.dialog_user_text)
                        .setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .create();

                alertDialog.show();

                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }


    @Override
    public void onRegisterClick(
            final String firstname,
            final String lastname,
            final String email,
            final String password,
            final String cgv,
            final String newsletter) {

        // password is not encrypted yet

        showDialog("Inscription à Midipile");

        midipileService.postRegister(firstname, lastname, email, password, cgv, newsletter,
                "android", MidipileUtilities.getUniquePsuedoID(), new Callback<User>() {
            @Override
            public void success(User u, Response response) {
                hideDialog();
                setUser(u, response.getHeaders());
            }

            @Override
            public void failure(RetrofitError error) {

                hideDialog();

                Log.i("fr.creads.midipile", error.toString());

                if(error.isNetworkError()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_register_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onRegisterClick(firstname,lastname,email,password,cgv,newsletter);
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else {
                    String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());

                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Map<String, List<String>>>>() {
                    }.getType());

                    try {
                        List<String> errorsEmail = (List<String>) ((Map)map.get("errors")).get("email");
                        Toast.makeText(getApplicationContext(), Joiner.on("\n").join(errorsEmail), Toast.LENGTH_SHORT).show();
                    } catch(Exception e){
                        Log.e(Constants.TAG, e.getMessage());
                    }
                }
            }
        });
    }


    public void setSharedUser(User user){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp.edit().putString(USER_SHAREDPREF, new Gson().toJson(user)).apply();
    }

    private void setUser(User u, List<Header> headers){
        Toast.makeText(getApplicationContext(), "Vous êtes connecté", Toast.LENGTH_LONG).show();
        user = u;
        user.setXwsseHeader(getResponseXwsseHeaders(headers));
        setSharedUser(user);
        mNavigationDrawerFragment.displayUser(u);

        if(null != getSelectedDeal()){
            onDealsSelected(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void updateUser(User u){
        user = u;
        setSharedUser(user);
        if( null != mNavigationDrawerFragment){
            mNavigationDrawerFragment.displayUser(user);
        }
    }

    private void updateUser(User u, List<Header> headers){
        user = u;
        user.setXwsseHeader(getResponseXwsseHeaders(headers));
        setSharedUser(user);
        if( null != mNavigationDrawerFragment){
            mNavigationDrawerFragment.displayUser(user);
        }
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

        return user;
    }

    public void showForgetPasswordDialog(){

        LayoutInflater inflater=HomeActivity.this.getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_forgetpassword,null);
        final EditText emailEditText=(EditText)layout.findViewById(R.id.forgetPasswordEditText);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        alertDialogBuilder
                .setTitle(R.string.dialog_forgetpassword_title)
                .setMessage(R.string.dialog_forgetpassword)
                .setView(layout)
                .setPositiveButton(R.string.dialog_forgetpassword_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Set an EditText view to get user input
                        postForgetPassword(emailEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.dialog_forgetpassword_no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void postForgetPassword(String email){
        midipileService.postForgetPassword(email, new Callback<Map<String, String>>() {
            @Override
            public void success(Map<String, String> message, Response response) {

                Toast.makeText(getApplicationContext(), message.get("success"), Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(RetrofitError error) {
                String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());

                Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Map<String, List<String>>>>() {
                }.getType());

                try {
                    List<String> errorsEmail = (List<String>) ((Map)map.get("errors")).get("email");
                    Toast.makeText(getApplicationContext(), Joiner.on("\n").join(errorsEmail), Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Log.e(Constants.TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onUserSave(final Map<String, String> userData) {
        showDialog("Enregistrement de vos informations");

        midipileService.putUser(
                user.getXwsseHeader(),
                userData.get("firstname"),
                userData.get("lastname"),
                userData.get("email"),
                userData.get("phone"),
                userData.get("adress"),
                userData.get("adressMore"),
                userData.get("postcode"),
                userData.get("city"),
                userData.get("password"),
                new Callback<User>() {
            @Override
            public void success(User u, Response response) {
                Toast.makeText(getApplicationContext(), "Vos coordonnées ont été modifiées", Toast.LENGTH_LONG).show();
                updateUser(u, response.getHeaders());
                hideDialog();
            }

            @Override
            public void failure(RetrofitError error) {


                if(error.isNetworkError()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_register_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onUserSave(userData);
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else {
                    String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());

                    try {
                        Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Map<String, List<String>>>>() {
                        }.getType());

                        List<String> errorsEmail = (List<String>) ((Map)map.get("errors")).get("email");
                        Toast.makeText(getApplicationContext(), Joiner.on("\n").join(errorsEmail), Toast.LENGTH_SHORT).show();

                    } catch (Exception e){
                        Log.e(Constants.TAG, e.getMessage());
                    }
                }

                hideDialog();
            }
        });
    }

    public void logoutUser(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp.edit().remove(USER_SHAREDPREF).apply();
        user = null;
        mNavigationDrawerFragment.hideUser();
        Toast.makeText(getApplicationContext(), "Vous êtes déconnecté", Toast.LENGTH_LONG).show();
        changeFragment(new HomeFragment(), 1);
    }


    public void logoutInvisibleUser(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp.edit().remove(USER_SHAREDPREF).apply();
        user = null;
        if(null != mNavigationDrawerFragment){
            mNavigationDrawerFragment.hideUser();
        }

        whishlist = null;
    }

    /**
     *
     */
    public void refreshUserChance(){

        if(null == user && user.getXwsseHeader().isEmpty()) {
            return;
        }

        midipileService.getChances(user.getXwsseHeader(), new Callback<Map<String, String>>() {

            @Override
            public void success(Map<String, String> stringStringMap, Response response) {

                try {
                    user.setChance(stringStringMap.get("chance"));
                    updateUser(user);
                } catch (Exception e){
                    Log.e(Constants.TAG, "Error while getting chance from refrech chance");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.isNetworkError()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_user_refresh_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    refreshUserChance();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                }else if(error.getResponse().getStatus() == 403){
                    logoutInvisibleUser();
                }

                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    /**
     * Refresh user data : make api call and save result in the activity. Also refresh navigation drawer
     */
    public void refreshUser(){
        if(null == user && user.getXwsseHeader().isEmpty()) {
            return;
        }

        midipileService.getLoggedUser(user.getXwsseHeader(), new Callback<User>() {

            @Override
            public void success(User u, Response response) {
                updateUser(u, response.getHeaders());
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.isNetworkError()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_user_refresh_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    refreshUser();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else if (error.getResponse().getStatus() == 403) {
                    logoutInvisibleUser();
                }
            }
        });
    }

    /**
     * Open login page
     */
    public void openLogin(){
        // loginRegister fragment set on position 8
        changeFragment( new LoginRegisterFragment(), 8);
    }

    /**
     * Called when user click on an participation button from any framgent
     * @param deal
     */
    @Override
    public void onParticipateClick(Deal deal) {


        if(null != user){
            // user logged
            if(user.getXwsseHeader().isEmpty()) {
                return;
            }

            // check adress filled and enough chance
            if(user.getRue().isEmpty() || user.getCode_postal().isEmpty() || user.getVille().isEmpty() ){


                OnClickWrapper onClickNoAdressWrapper = new OnClickWrapper("noAdressParticipation", new SuperToast.OnClickListener() {
                    @Override
                    public void onClick(View view, Parcelable token) {
                        /* On click event */
                        // loginRegister fragment set on position 8
                        changeFragment( new UserFragment(), 0);
                    }
                });

                SuperActivityToast superActivityToast = new SuperActivityToast(this, SuperToast.Type.BUTTON);
                superActivityToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                superActivityToast.setButtonIcon(SuperToast.Icon.Dark.EDIT, "COMPLÉTER");
                superActivityToast.setText(getString(R.string.deal_need_user_adress));
                superActivityToast.setOnClickWrapper(onClickNoAdressWrapper);
                superActivityToast.show();

            } else if(user.getChance() <= 0){
                SuperActivityToast.create(this, "Vous n'avez plus de chance.", SuperToast.Duration.LONG).show();
            } else {
                postParticipation(deal);
            }


        } else {
            // loginRegister fragment set on position 8
            changeFragment( new LoginRegisterFragment(), 8);
        }


    }

    /**
     * Post the deal participation to the API
     * On success : set toast and display popup to choose the app to open if any
     *
     * @param deal
     */
    public void postParticipation(Deal deal){


        showDialog("Participation en cours");

        midipileService.postPlayDeal(user.getXwsseHeader(), deal.getId(), new Callback<Map<String, String>>() {

            @Override
            public void success(Map<String, String> mapData, Response response) {
                hideDialog();

                // send event participate
                ((MidipileApplication)getApplication()).sendEventTracking(
                        R.string.tracker_deal_category,
                        R.string.tracker_deal_action_participate,
                        getSelectedDeal().getNom());

                try {

                    SuperActivityToast successSuperToast = new SuperActivityToast(HomeActivity.this);
                    successSuperToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                    successSuperToast.setBackground( R.drawable.toast_success );
                    successSuperToast.setText( getResources().getString(R.string.deal_participate_success) );
                    successSuperToast.setTextColor(Color.WHITE);
                    successSuperToast.setTouchToDismiss(true);
                    successSuperToast.setAnimations(SuperToast.Animations.FLYIN);
                    successSuperToast.show();

                } catch (Exception e){
                    Log.d(Constants.TAG, "Erreur lors de la participation : 200 " + e.getMessage());
                }

                try {
                    user.setChance(mapData.get("chance"));
                    updateUser(user);
                } catch (Exception e){
                    Log.e(Constants.TAG, "Error while getting chance after participating");
                }


                if( !getSelectedDeal().getPlayStore().isEmpty()){
                    String message = getResources().getString(R.string.deal_participate_share_message) +
                            " " + getSelectedDeal().getSociete() +
                            " " + getString(R.string.deal_participate_share_message_suffix) + " ?";

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setMessage(message)
                            .setPositiveButton(R.string.dialog_deal_participate_share_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getSelectedDeal().getPlayStore())));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getSelectedDeal().getPlayStore())));
                                    }
                                }
                            })
                            .setNegativeButton(R.string.dialog_deal_participate_share_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                }

                // set alarm few days after the participation
                startNotificationAlarmManager();

            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();

                if (error.isNetworkError()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_user_refresh_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    postParticipation(getSelectedDeal());
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else if(error.getResponse().getStatus() == 403){
                    Toast.makeText(getApplicationContext(), "Veuillez vous reconnecter à Midipile.", Toast.LENGTH_SHORT).show();
                    logoutInvisibleUser();
                }

                Log.i("fr.creads.midipile", error.getUrl());
                Log.i("fr.creads.midipile", error.toString());
            }
        });

    }

    /**
     * Set notification to be open in the next 1 day and 5 days after the participation
     * Called after success full participation, alarm is reset if two participation succesivly
     */
    public void startNotificationAlarmManager(){

        int howMuchDayAfter = 1;
        int secondNotificationDayAfter = 5;

        Calendar currentCalendar = new GregorianCalendar();

        Calendar dayCalendar = new GregorianCalendar();
        dayCalendar.add(Calendar.DAY_OF_YEAR, currentCalendar.get(Calendar.DAY_OF_YEAR));
        dayCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
        dayCalendar.set(Calendar.HOUR_OF_DAY, currentCalendar.get(Calendar.HOUR_OF_DAY));
        dayCalendar.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE));
        dayCalendar.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND));
        dayCalendar.set(Calendar.DATE, currentCalendar.get(Calendar.DATE)  + howMuchDayAfter );
        dayCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));

        int dayOfWeek = dayCalendar.get(Calendar.DAY_OF_WEEK);

        Intent newIntent = new Intent(getApplicationContext() , ParticipateNotificationBroadcastReceiver.class);
        PendingIntent pendingIntent1  = PendingIntent.getBroadcast(this, ONEDAY_NOTIFICATION_ALARM, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am1.set(AlarmManager.RTC_WAKEUP, dayCalendar.getTimeInMillis(), pendingIntent1);


        //Log.d(Constants.TAG, "AlarmManagerTest: : " + dayCalendar.get(Calendar.DATE) + "-"
        //        + (dayCalendar.get(Calendar.MONTH) + 1)+ "-" + dayCalendar.get(Calendar.YEAR) + " "
        //        + dayCalendar.get(Calendar.HOUR_OF_DAY) + ":" + dayCalendar.get(Calendar.MINUTE) + ":"
        //        + dayCalendar.get(Calendar.SECOND) + "." + dayCalendar.get(Calendar.MILLISECOND));





        Calendar dayCalendar2 = new GregorianCalendar();
        dayCalendar2.add(Calendar.DAY_OF_YEAR, currentCalendar.get(Calendar.DAY_OF_YEAR));
        dayCalendar2.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
        dayCalendar2.set(Calendar.HOUR_OF_DAY, currentCalendar.get(Calendar.HOUR_OF_DAY));
        dayCalendar2.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE));
        dayCalendar2.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND));
        dayCalendar2.set(Calendar.DATE, currentCalendar.get(Calendar.DATE)  + secondNotificationDayAfter );
        dayCalendar2.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));

        int dayOfWeek2 = dayCalendar2.get(Calendar.DAY_OF_WEEK);

        if(dayOfWeek2 == Calendar.SATURDAY){
            dayCalendar2.set(Calendar.DATE, dayCalendar2.get(Calendar.DATE) +2);
        } else if(dayOfWeek2 == Calendar.SUNDAY){
            dayCalendar2.set(Calendar.DATE, dayCalendar2.get(Calendar.DATE) +1);
        }

        Intent newIntent2 = new Intent(getApplicationContext() , ParticipateNotificationBroadcastReceiver.class);
        PendingIntent pendingIntent2  = PendingIntent.getBroadcast(this, FIVEDAY_NOTIFICATION_ALARM, newIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am2.set(AlarmManager.RTC_WAKEUP, dayCalendar2.getTimeInMillis(), pendingIntent2);


    }




    /**
     * Load list of badge. One loaded, notify UserFragment which called UserBadgeFragment
     */
    public void loadBadgesList(){

        if(null != badges && !badges.isEmpty()){
            return;
        }

        midipileService.getBadges(new Callback<ArrayList<Badge>>() {

            @Override
            public void success(ArrayList<Badge> badgeItems, Response response) {
                badges = badgeItems;

                UserFragment userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag(UserFragment.class.getName());

                if (!(userFragment instanceof OnBadgesLoadedListener)) {
                    throw new IllegalStateException(
                      "Fragment must implement the OnBadgesLoadedListener.");
                } else {
                    mBadgesLoadedCallbacks = (OnBadgesLoadedListener) userFragment;
                    mBadgesLoadedCallbacks.onBadgeLoaded(badges);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error.isNetworkError()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_network_error_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadBadgesList();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                }

                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    /**
     * Get the list of badges
     * @return List<Badge>, empty list if no badge
     */
    public List<Badge> getBadges(){
        if(null == badges) {
            return new ArrayList<Badge>();
        } else {
            return badges;
        }

    }


    public void loadWhishList(){
        loadWhishList(0, 0);
    }
    public void loadWhishList(int from, int length){

        if(null == whishlist){
            whishlist = new ArrayList<Deal>();
        }

        if(from == 0){
            from = 0;
        }
        if(length == 0){
            length = 0;
        }

        midipileService.getWhishlist(user.getXwsseHeader(), from, length, new Callback<List<Deal>>() {

            @Override
            public void success(List<Deal> deals, Response response) {

                whishlist.addAll(deals);

                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());

                if (!(homeFragment instanceof OnDataLoadedListener)) {
                    Log.d(Constants.TAG, "Fragment must implement the OnDataLoadedListener for Whishlist.");
                } else {
                    OnDataLoadedListener mLoadedWhishlistCallbacks = (OnDataLoadedListener) homeFragment;
                    mLoadedWhishlistCallbacks.onDataLoaded();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.isNetworkError()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_network_error_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadWhishList();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                }

                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    /**
     * Get the whishlist which is a list of deal
     * @return List<Deal>
     */
    public List<Deal> getWhishlist(){
        return whishlist;
    }




    /**
     * Call midipile service to load the last 40 deals with winners
     * Call onDataLoadedListener for telling WinnersFragments that data is loaded
     */
    public void loadWinnersList(){

        if(null == winnersDeals){
            winnersDeals = new ArrayList<Deal>();
        }

        midipileService.getWinnersDeals(new Callback<Deals>() {

            @Override
            public void success(Deals deals, Response response) {

                winnersDeals.addAll(deals.getDeals());

                WinnersFragment winnersFragment = (WinnersFragment) getSupportFragmentManager().findFragmentByTag(WinnersFragment.class.getName());

                if (!(winnersFragment instanceof OnDataLoadedListener)) {
                    Log.d(Constants.TAG, "Fragment must implement the OnDataLoadedListener  for Winners.");
                } else {
                    OnDataLoadedListener mLoadedWinnersCallbacks = (OnDataLoadedListener) winnersFragment;
                    mLoadedWinnersCallbacks.onDataLoaded();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.isNetworkError()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle(R.string.dialog_network_error_title)
                            .setMessage(R.string.dialog_network_error)
                            .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadWinnersList();
                                }
                            })
                            .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create();

                    alertDialog.show();
                } else {
                    SuperActivityToast successSuperToast = new SuperActivityToast(HomeActivity.this);
                    successSuperToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                    successSuperToast.setText( getResources().getString(R.string.toast_server_error_winners) );
                    successSuperToast.show();
                }

                Log.i("fr.creads.midipile", error.toString());
            }
        });
    }

    /**
     * Get the winners deals list
     * @return List<Deal>
     */
    public List<Deal> getWinnersDeals(){
        return winnersDeals;
    }
}

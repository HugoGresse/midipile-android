package fr.creads.midipile.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import java.util.List;
import java.util.Map;

import fr.creads.midipile.R;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.api.MidipileAPI;
import fr.creads.midipile.fragments.DealFragment;
import fr.creads.midipile.fragments.DealProductFragment;
import fr.creads.midipile.fragments.DealsDayFragment;
import fr.creads.midipile.fragments.HomeFragment;
import fr.creads.midipile.fragments.LastWinnerFragment;
import fr.creads.midipile.fragments.LoginRegisterFragment;
import fr.creads.midipile.fragments.LoginRegisterLoginFragment;
import fr.creads.midipile.fragments.LoginRegisterRegisterFragment;
import fr.creads.midipile.fragments.UserAdressFragment;
import fr.creads.midipile.fragments.UserFragment;
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
import retrofit.mime.TypedByteArray;

public class HomeActivity extends FragmentActivity
        implements
            NavigationDrawerFragment.NavigationDrawerCallbacks,
            DealsDayFragment.onDealsSelectedListener,
            DealProductFragment.onButtonParticipateClickListener,
            LoginRegisterLoginFragment.onButtonClickListener,
            LoginRegisterRegisterFragment.onRegisterButtonClickListener,
            UserAdressFragment.OnUserUpdateListener{

    private static final String USER_SHAREDPREF = "userlogged";

    private MidipileAPI midipileService;

    private ArrayList<Deal> deals;
    private int dealPosition;

    /**
     * The user connnected to the app
     */
    private User user;


    private SimpleFacebook mSimpleFacebook;

    private ProgressDialog mProgressDialog;

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
            case -1:
                // click on user profil
                changeFragment( new UserFragment(), position);
                break;
            case 0:
                changeFragment( new HomeFragment(), position);
                break;
            case 1:
                changeFragment( new LoginRegisterFragment(), position);
                break;
            case 2:
                changeFragment( new LastWinnerFragment(), position);
                break;
            case 3:
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
        if (!(frag instanceof OnDataLoadedListener)) {
            Log.e(Constants.TAG, "Fragment must implement the onDataLoadedListener.");
            // throw new IllegalStateException(
            // "Fragment must implement the onDataLoadedListener.");
        } else {
            mLoadedCallbacks = (OnDataLoadedListener) frag;
        }

        String backStateName =  frag.getClass().getName();
        String fragmentTag = backStateName;


        try {

            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(enter, exit, pop_enter, pop_exit);
                transaction.replace(R.id.container, frag, Integer.toString(position));
                transaction.addToBackStack(backStateName);
                transaction.commit();
                Log.d(Constants.TAG, "addToBackTack");
            } else {
                Log.d(Constants.TAG, "Remove from back stack");
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                alertDialogBuilder.setTitle(R.string.dialog_network_error_title);
                alertDialogBuilder.setMessage(R.string.dialog_network_error);

                alertDialogBuilder.setPositiveButton(R.string.dialog_network_error_ok,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        loadLastDeals();
                    }
                });

                alertDialogBuilder.setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

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

        midipileService.postLogin(email, password, new Callback<User>() {
            @Override
            public void success(User u, Response response) {

                hideDialog();

                setUser(u);
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

        midipileService.postLoginFacebook(email, fid, fid, firstname, lastname, "1", new Callback<User>() {
            @Override
            public void success(User u, Response response) {
                setUser(u);

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

        midipileService.postRegister(firstname, lastname, email, password, cgv, newsletter, new Callback<User>() {
            @Override
            public void success(User u, Response response) {

                hideDialog();

                setUser(u);
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


                    alertDialogBuilder.setPositiveButton(R.string.dialog_network_error_ok,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            loadLastDeals();
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.dialog_network_error_no,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

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

    private void setUser(User u){
        Toast.makeText(getApplicationContext(), "Vous êtes connecté", Toast.LENGTH_LONG).show();

        setSharedUser(u);
        mNavigationDrawerFragment.displayUser(u);

        Log.i(Constants.TAG, getUser().toString());

        if(null != getSelectedDeal()){
            onDealsSelected(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
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

        Log.d(Constants.TAG, Integer.toString(user.getChance()));

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
    public void onUserSave() {
        Log.d(Constants.TAG, "onuserSave listener homeActivity");
    }

    public void logoutUser(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp.edit().remove(USER_SHAREDPREF).apply();
        user = null;
        mNavigationDrawerFragment.hideUser();
        Toast.makeText(getApplicationContext(), "Vous êtes déconnecté", Toast.LENGTH_LONG).show();
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


}

package fr.creads.midipile.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class LoginRegisterLoginFragment extends Fragment{

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    private ScrollView loginScrollView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button forgetPasswordButton;
    private Button loginButton;
    private Button loginFacebookButton;

    private TextView connectTextView;
    private TextView helperFacebookTextView;
    private ImageView bgImage;

    private onButtonClickListener mClickButtonListener;

    private SimpleFacebook mSimpleFacebook;

    public interface onButtonClickListener {
        public void onLoginClick(String email, String password);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mSimpleFacebook = SimpleFacebook.getInstance();

        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loginregister_login, container, false);

        loginScrollView = (ScrollView) rootView.findViewById(R.id.loginScrollView);
        emailEditText = (EditText) rootView.findViewById(R.id.userEmailEditText);
        passwordEditText = (EditText) rootView.findViewById(R.id.userPasswordEditText);
        forgetPasswordButton = (Button) rootView.findViewById(R.id.userPasswordForgetButton);
        loginButton = (Button) rootView.findViewById(R.id.userLoginButton);
        loginFacebookButton = (Button) rootView.findViewById(R.id.userFacebookLoginButton);
        connectTextView = (TextView) rootView.findViewById(R.id.connectTextView);
        helperFacebookTextView = (TextView) rootView.findViewById(R.id.helperFbTextView);
        //        setInsets(getActivity(), bgImage);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        passwordEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        forgetPasswordButton.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        connectTextView.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        helperFacebookTextView.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());

        loginFacebookButton.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());
        loginButton.setTypeface(((HomeActivity)getActivity()).getLatoBoldTypeface());

        setButtonListener();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mClickButtonListener = (onButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onButtonClickListener");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(getActivity(), requestCode, resultCode, data);
    }


    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

    private void setButtonListener(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventClick();
            }
        });

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).showForgetPasswordDialog();
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    sendEventClick();
                    return true;
                }
                return false;
            }
        });


        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if user is already logged
                if( !mSimpleFacebook.isLogin()){
                    mSimpleFacebook.login(new OnLoginListener() {
                        @Override
                        public void onLogin() {
                            // change the state of the button or do whatever you want
                            Log.d(Constants.TAG, "Logged in ================");
                            getFacebookProfile();
                        }

                        @Override
                        public void onNotAcceptingPermissions(Permission.Type type) {
                            // user didn't accept READ or WRITE permission
                            Log.w(Constants.TAG, String.format("You didn't accept %s permissions", type.name()));
                        }

                        @Override
                        public void onThinking() {

                            Log.d(Constants.TAG, "on htinking");
                        }

                        @Override
                        public void onException(Throwable throwable) {

                            Log.d(Constants.TAG, "on exception");
                        }

                        @Override
                        public void onFail(String s) {

                            Log.d(Constants.TAG, "on fail");
                        }
                    });
                } else {

                    getFacebookProfile();
                }


            }
        });

    }

    private void sendEventClick(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(email.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre adresse email",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(password.isEmpty()) {
            Toast.makeText(getActivity(), "Merci d'entrer votre mot de passe",
                    Toast.LENGTH_LONG).show();
            return;
        }


        mClickButtonListener.onLoginClick(email, password);
    }

    private void fbLogin(){

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
                showDialog();
            }
            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                Log.d(Constants.TAG, throwable.getMessage());
            }
            @Override
            public void onFail(String reason) {
                hideDialog();
                Log.d(Constants.TAG, "failed");
            }
            @Override
            public void onComplete(Profile response) {
                hideDialog();
                Log.d(Constants.TAG, response.getId());
                Log.d(Constants.TAG, response.getEmail());
                Log.d(Constants.TAG, response.getFirstName());
                Log.d(Constants.TAG, response.getLastName());
            }
        });
    }

    private ProgressDialog mProgressDialog;
    protected void showDialog() {
        if (mProgressDialog == null) {
            setProgressDialog();
        }
        mProgressDialog.show();
    }
    protected void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void setProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Connexion à Facebook.");
        mProgressDialog.setMessage("Chargement...");
    }

}

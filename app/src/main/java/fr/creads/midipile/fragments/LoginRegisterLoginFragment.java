package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;

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

    public interface onButtonClickListener {
        public void onLoginClick(String email, String password);
        public void sendFacebookLoginClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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
                mClickButtonListener.sendFacebookLoginClick();
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




}

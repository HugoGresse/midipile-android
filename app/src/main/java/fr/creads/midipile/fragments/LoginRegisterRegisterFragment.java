package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class LoginRegisterRegisterFragment extends Fragment{

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;


    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox newsletterCheckBox;
    private CheckBox cgvCheckBox;
    private Button loginButton;
    private Button loginFacebookButton;

    private TextView connectTextView;
    private TextView helperFacebookTextView;


    private onRegisterButtonClickListener mClickButtonListener;

    public interface onRegisterButtonClickListener {
        public void onRegisterClick(
                String firstname,
                String lastname,
                String email,
                String password,
                String cgv,
                String newsletter);
        public void onRegisterFacebook();
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

        View rootView = inflater.inflate(R.layout.fragment_loginregister_register, container, false);

        lastnameEditText = (EditText) rootView.findViewById(R.id.userNameEditText);
        firstnameEditText = (EditText) rootView.findViewById(R.id.userFirstNameEditText);
        emailEditText = (EditText) rootView.findViewById(R.id.userEmailEditText);
        passwordEditText = (EditText) rootView.findViewById(R.id.userPasswordEditText);
        newsletterCheckBox = (CheckBox) rootView.findViewById(R.id.newsletterCheckBox);
        cgvCheckBox = (CheckBox) rootView.findViewById(R.id.cgvCheckBox);
        loginButton = (Button) rootView.findViewById(R.id.userLoginButton);
        loginFacebookButton = (Button) rootView.findViewById(R.id.userFacebookLoginButton);
        helperFacebookTextView = (TextView) rootView.findViewById(R.id.helperFbTextView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lastnameEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        firstnameEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        emailEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        passwordEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        newsletterCheckBox.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        cgvCheckBox.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        helperFacebookTextView.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());

        loginFacebookButton.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());
        loginButton.setTypeface(((HomeActivity)getActivity()).getLatoBoldTypeface());

        setButtonListener();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mClickButtonListener = (onRegisterButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onRegisterButtonClickListener");
        }
    }


    private void setButtonListener(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventClick();
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
                mClickButtonListener.onRegisterFacebook();
            }
        });

    }


    private void sendEventClick(){
        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String cgv;
        String newsletter = "";

        if(!cgvCheckBox.isChecked()){
            Toast.makeText(getActivity(), "Vous devez accepter les conditions générales d'utilisation",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            cgv = "1";
        }

        if(newsletterCheckBox.isChecked()){
            newsletter = "1";
        }

        if(firstname.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre prénom",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(email.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre nom",
                    Toast.LENGTH_LONG).show();
            return;
        }

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

        mClickButtonListener.onRegisterClick(firstname, lastname, email, password, cgv, newsletter);
    }




}

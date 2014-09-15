package fr.creads.midipile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private Button loginButton;
    private Button loginFacebookButton;

    private TextView connectTextView;
    private TextView helperFacebookTextView;


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
        helperFacebookTextView.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());

        loginFacebookButton.setTypeface(((HomeActivity) getActivity()).getLatoTypeface());
        loginButton.setTypeface(((HomeActivity)getActivity()).getLatoBoldTypeface());
    }


}

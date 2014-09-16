package fr.creads.midipile.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserAdressFragment extends Fragment {

    private OnUserUpdateListener mUserUpdateCallback;

    public interface OnUserUpdateListener {
        public void onUserSave();
    }

    private ScrollView adressScrollView;
    private TextView titleTextView;
    private EditText userFirstnameEditText;
    private EditText userLastnameEditText;
    private EditText userEmailEditText;
    private EditText userPhoneEditText;
    private EditText userAdressEditText;
    private EditText userAdressMoreEditText;
    private EditText userPostalcodeEditText;
    private EditText userCityEditText;
    private EditText userPasswordEditText;
    private Button userLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_adress, container, false);

        adressScrollView = (ScrollView) rootView.findViewById(R.id.loginScrollView);
        titleTextView = (TextView) rootView.findViewById(R.id.userAdressTitle);
        userFirstnameEditText = (EditText) rootView.findViewById(R.id.userFirstnameEditText);
        userLastnameEditText = (EditText) rootView.findViewById(R.id.userLastnameEditText);
        userEmailEditText = (EditText) rootView.findViewById(R.id.userEmailEditText);
        userPhoneEditText = (EditText) rootView.findViewById(R.id.userPhoneEditText);
        userAdressEditText = (EditText) rootView.findViewById(R.id.userAdressEditText);
        userAdressMoreEditText = (EditText) rootView.findViewById(R.id.userAdressMoreEditText);
        userPostalcodeEditText = (EditText) rootView.findViewById(R.id.userPostalcodeEditText);
        userCityEditText = (EditText) rootView.findViewById(R.id.userCityEditText);
        userPasswordEditText = (EditText) rootView.findViewById(R.id.userPasswordEditText);
        userLoginButton = (Button) rootView.findViewById(R.id.userLoginButton);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userFirstnameEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userLastnameEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userEmailEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userPhoneEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userAdressEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userAdressMoreEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userPostalcodeEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userCityEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userPasswordEditText.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
        userLoginButton.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());

        setInsets(getActivity(), adressScrollView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mUserUpdateCallback = (OnUserUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserUpdateListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            // keyboard visible
            adressScrollView.setPadding(0, 0, 0, 0);
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            setInsets(getActivity(), adressScrollView);
        }
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getPixelInsetBottom());
    }

}

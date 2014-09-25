package fr.creads.midipile.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.objects.User;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserProfilFragment extends Fragment {

    private OnUserUpdateListener mUserUpdateCallback;

    public interface OnUserUpdateListener {
        public void onUserSave(Map<String, String> data);
    }

    private User user;

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
    private Button userSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        user = ((HomeActivity)getActivity()).getUser();
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

        userSaveButton = (Button) rootView.findViewById(R.id.userSaveButton);

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
        userSaveButton.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());


        userFirstnameEditText.setText(user.getPrenom());
        userLastnameEditText.setText(user.getNom());
        userEmailEditText.setText(user.getEmail());
        userPhoneEditText.setText(user.getMobile());
        userAdressEditText.setText(user.getRue());
        userAdressMoreEditText.setText(user.getRue_bis());
        userPostalcodeEditText.setText(user.getCode_postal());
        userCityEditText.setText(user.getVille());



        setInsets(getActivity(), adressScrollView);
        userSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventClick();
            }
        });
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

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getPixelInsetBottom());
    }

    private void sendEventClick(){
        String firstname = userFirstnameEditText.getText().toString();
        String lastname = userLastnameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String phone = userPhoneEditText.getText().toString();
        String adress = userAdressEditText.getText().toString();
        String adressMore = userAdressMoreEditText.getText().toString();
        String postcode = userPostalcodeEditText.getText().toString();
        String city = userCityEditText.getText().toString();
        String password = userPasswordEditText.getText().toString();



        if(firstname.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre prénom", Toast.LENGTH_LONG).show();
            return;
        }
        if(lastname.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre nom", Toast.LENGTH_LONG).show();
            return;
        }
        if(email.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre adresse email", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre numéro de téléphone", Toast.LENGTH_LONG).show();
            return;
        }
        if(adress.isEmpty()) {
            Toast.makeText(getActivity(), "Merci de remplir votre adresse", Toast.LENGTH_LONG).show();
            return;
        }
        if(postcode.isEmpty()) {
            Toast.makeText(getActivity(), "Merci d'entrer votre code postal", Toast.LENGTH_LONG).show();
            return;
        }
        if(city.isEmpty()) {
            Toast.makeText(getActivity(), "Merci d'entrer votre ville", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("firstname", firstname);
        data.put("lastname", lastname);
        data.put("email", email);
        data.put("phone", phone);
        data.put("adress", adress);
        data.put("adressMore", adressMore);
        data.put("postcode", postcode);
        data.put("city", city);

        if(!password.isEmpty()) {
            data.put("password", password);
        }

        mUserUpdateCallback.onUserSave(data);
    }




}

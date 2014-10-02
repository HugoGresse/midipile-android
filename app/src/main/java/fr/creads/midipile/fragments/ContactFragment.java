package fr.creads.midipile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.utilities.MidipileUtilities;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class ContactFragment extends Fragment{

    private static final String SCREEN_NAME = "Contact";

    private ScrollView contactScrollView;
    private EditText name;
    private EditText email;
    private EditText message;
    private Button sendButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);


        contactScrollView = (ScrollView) rootView.findViewById(R.id.contactScrollView);
        name = (EditText) rootView.findViewById(R.id.nameEditText);
        email = (EditText) rootView.findViewById(R.id.emailEditText);
        message = (EditText) rootView.findViewById(R.id.messageEditText);
        sendButton = (Button) rootView.findViewById(R.id.sendButton);

        MidipileUtilities.setInsetsWithNoTab(getActivity(), contactScrollView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        message.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    submitForm();
                    return true;
                }
                return false;
            }
        });

    }

    private void submitForm(){
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String messageString = message.getText().toString();

        if(nameString.isEmpty()){
            SuperActivityToast.create(getActivity(), "Merci d'entrer votre nom", SuperToast.Duration.LONG).show();
            return;
        }
        if(emailString.isEmpty()){
            SuperActivityToast.create(getActivity(), "Merci d'entrer votre email", SuperToast.Duration.LONG).show();
            return;
        }
        if(messageString.isEmpty()){
            SuperActivityToast.create(getActivity(), "Merci d'entrer votre message", SuperToast.Duration.LONG).show();
            return;
        }

        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);

        ((HomeActivity)getActivity()).sendContact(nameString, emailString, messageString);

    }


}

package fr.creads.midipile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

import fr.creads.midipile.R;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class ContactFragment extends Fragment{

    private static final String SCREEN_NAME = "Contact";

    private EditText name;
    private EditText email;
    private EditText message;
    private Button sendButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);


        name = (EditText) rootView.findViewById(R.id.nameEditText);
        email = (EditText) rootView.findViewById(R.id.emailEditText);
        message = (EditText) rootView.findViewById(R.id.messageEditText);
        sendButton = (Button) rootView.findViewById(R.id.sendButton);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().isEmpty()){
                    SuperActivityToast.create(getActivity(), "Merci d'entrer votre nom", SuperToast.Duration.LONG).show();
                    return;
                }
                if(email.getText().toString().isEmpty()){
                    SuperActivityToast.create(getActivity(), "Merci d'entrer votre email", SuperToast.Duration.LONG).show();
                    return;
                }
                if(message.getText().toString().isEmpty()){
                    SuperActivityToast.create(getActivity(), "Merci d'entrer votre message", SuperToast.Duration.LONG).show();
                    return;
                }

//                ((HomeActivity)getActivity()).openLogin();
            }
        });
    }

}

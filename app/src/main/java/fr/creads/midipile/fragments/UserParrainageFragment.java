package fr.creads.midipile.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.User;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserParrainageFragment extends Fragment {

    private ImageButton shareCodeButton;
    private TextView parrainageCodeTextView;
    private TextView filleulsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_parrainage, container, false);

        shareCodeButton = (ImageButton) rootView.findViewById(R.id.shareParrainageButton);
        parrainageCodeTextView = (TextView) rootView.findViewById(R.id.parrainageCodeTextView);
        filleulsTextView = (TextView) rootView.findViewById(R.id.filleulsListTextView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String userParrainageCode = ((HomeActivity)getActivity()).getUser().getParrainageCode();

        parrainageCodeTextView.setText(userParrainageCode);


        shareCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(
                        android.content.Intent.EXTRA_TEXT,
                        getResources().getString(R.string.user_parrainage_share) + " " + userParrainageCode + " " +  Constants.URL_SITE);

                startActivity(Intent.createChooser(
                        sharingIntent,
                        getResources().getString(R.string.user_parrainage_share_title)));
            }
        });


        List<User> filleuls =  ((HomeActivity)getActivity()).getUser().getFilleuls();
        String filleulsString = "";

        for(User u : filleuls){
            filleulsString += u.getPrenom() + " " + u.getNom() + "\n";
        }

        filleulsTextView.setText(filleulsString);
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

}

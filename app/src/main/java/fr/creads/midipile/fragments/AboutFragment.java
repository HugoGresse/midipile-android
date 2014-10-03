package fr.creads.midipile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.utilities.MidipileUtilities;

/**
 * Created by Hugo Gresse
 * Date : 03/10/2014.
 */
public class AboutFragment extends Fragment {


    private static final String SCREEN_NAME = "A propos";

    private ScrollView aboutScrollView;
    private TextView librariesListTextView;

    @Override
    public void onResume (){
        super.onResume();
        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);


        librariesListTextView = (TextView)rootView.findViewById(R.id.about_libraries);
        aboutScrollView = (ScrollView)rootView.findViewById(R.id.aboutScrollView);

        MidipileUtilities.setInsetsWithNoTab(getActivity(), aboutScrollView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StringBuilder builder = new StringBuilder();
        String[] libraries = getResources().getStringArray(R.array.about_libraries);
        for (String s : libraries){
            builder.append(s+" \n");
            librariesListTextView.setText(builder.toString());
        }

    }


}

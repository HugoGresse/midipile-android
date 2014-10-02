package fr.creads.midipile.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.User;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserParrainageFragment extends Fragment {

    public static final String PARRAINAGE_ARGS = "parrainage";

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

        final String shareMessage = getResources().getString(R.string.user_parrainage_share) +
                " " + userParrainageCode + " " +  Constants.URL_SITE + "/?p=" + userParrainageCode;

        shareCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareIt(
                        getActivity(),
                        shareMessage,
                        getResources().getString(R.string.user_parrainage_share_title),
                        userParrainageCode);

            }
        });


        List<User> filleuls =  ((HomeActivity)getActivity()).getUser().getFilleuls();
        String filleulsString = "";

        for(User u : filleuls){
            filleulsString += u.getPrenom() + " " + u.getNom() + "\n";
        }

        filleulsTextView.setText(filleulsString);
    }

    /**
     * Open share dialog and change facebook share test to just url
     * @param activity
     * @param message
     * @param title
     */
    private void shareIt(Activity activity, String message, String title, String parrainageCode){
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for(final ResolveInfo app : activityList) {

            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("text/plain");
            if(TextUtils.equals(packageName, "com.facebook.katana")){
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.URL_SITE + "/?p=" + parrainageCode);
            } else {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            }

            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);

        }

        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), title);

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);

    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

}

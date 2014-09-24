package fr.creads.midipile.fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.BadgesAdapter;
import fr.creads.midipile.objects.Badge;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserBadgeFragment extends Fragment{

    private static final String SCREEN_NAME = UserFragment.SCREEN_NAME + "Badges";

    private View headerView;
    private ListView badgeListView;

    private TextView mainTitleTextView;

    private List<Badge> badges;
    private List<Badge> userBadges;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((HomeActivity)getActivity()).loadBadgesList();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        badges =  ((HomeActivity)getActivity()).getBadges();
        userBadges = ((HomeActivity)getActivity()).getUser().getBadges();

        View rootView = inflater.inflate(R.layout.fragment_user_badge, container, false);
        badgeListView = (ListView) rootView.findViewById(R.id.badgeListView);
        mainTitleTextView = (TextView) rootView.findViewById(R.id.mainTitleTextView);

        if(null != badges){
            setBadgeAdapter(badges);
        }
        return rootView;
    }

    @Override
    public void onResume (){
        super.onResume();
        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainTitleTextView.setTypeface(((HomeActivity)getActivity()).getLatoTypeface());
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

    public void setBadgeAdapter(List<Badge> badges){
        if(null != badgeListView){

            // set user selected badge to true
            for (int i = 0; i < userBadges.size(); i++) {
                Badge elementOne = userBadges.get(i);
                if (badges.contains(elementOne)) {
                    badges.get(badges.indexOf(elementOne)).setUserBadge(true);
                }
            }

            badgeListView.setAdapter(new BadgesAdapter(getActivity(), badges));
            badgeListView.setSelector(new ColorDrawable(0));
        }
    }

}

package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.BadgesAdapter;
import fr.creads.midipile.objects.Badge;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserBadgeFragment extends Fragment{

    private ListView badgeListView;

    private List<Badge> badges;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((HomeActivity)getActivity()).loadBadgesList();

        badges =  ((HomeActivity)getActivity()).getBadges();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_badge, container, false);
        badgeListView = (ListView) rootView.findViewById(R.id.badgeListView);

        if(null != badges){
            setBadgeAdapter(badges);
        }

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

    public void setBadgeAdapter(List<Badge> badges){
        if(null != badgeListView){
            badgeListView.setAdapter(new BadgesAdapter(getActivity().getApplicationContext(), badges));
        }
    }

}

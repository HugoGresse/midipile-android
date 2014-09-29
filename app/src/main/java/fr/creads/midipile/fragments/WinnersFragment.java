package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.WinnersAdapter;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 23/09/14
 */
public class WinnersFragment extends Fragment implements OnDataLoadedListener {

    private static final String SCREEN_NAME = "Winners";

    private ListView winnersListView;
    private ProgressBar myProgressBar;

    private ArrayList<Deal> dealsList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // call homeActivity to load winers data
        ((HomeActivity)getActivity()).loadWinnersList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_winners, container, false);

        winnersListView = (ListView) rootView.findViewById(R.id.winnersListView);
        myProgressBar = (ProgressBar) rootView.findViewById(R.id.winnersProgressBar);
        setInsets(getActivity(), winnersListView);

        return rootView;
    }

    @Override
    public void onResume (){
        super.onResume();
        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((HomeActivity)getActivity()).getWinnersDeals() != null){
            setAdapters((ArrayList<Deal>) ((HomeActivity)getActivity()).getWinnersDeals());
        }

    }

    public void setAdapters(ArrayList<Deal> deals){

        if(deals != null && deals.isEmpty()){

        } else {
            myProgressBar.setVisibility(View.GONE);
            dealsList = deals;
            winnersListView.setAdapter(new WinnersAdapter(getActivity().getApplicationContext(), dealsList));
        }
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getActionBarHeight() + config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
    }

    @Override
    public void onDataLoaded() {
        setAdapters((ArrayList<Deal>) ((HomeActivity) getActivity()).getWinnersDeals());
    }
}

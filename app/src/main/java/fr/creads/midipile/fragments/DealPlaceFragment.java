package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealPlaceFragment extends Fragment
        implements MidipileSupportMapFragment.onGoogleMapLoadedListener{

    private Deal deal;

    private View rootView;
    private FrameLayout mapFrameLayout;
    private Button eshop;
    private Button website;

    private Fragment xmlFragment;

    private MidipileSupportMapFragment mapFragment;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setGoogleMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.fragment_deal_place, container, false);
            mapFrameLayout = (FrameLayout) rootView.findViewById((R.id.map));
            eshop = (Button) rootView.findViewById((R.id.mapEshop));
            website = (Button) rootView.findViewById((R.id.mapWebsite));

        } catch (InflateException e) {
            /* map is already there, just return view as it is */
            Log.e(Constants.TAG, e.toString());

        }

        return rootView;
    }

    private void setGoogleMapFragment(){
        mapFragment = (MidipileSupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        // We only create a fragment if it doesn't already exist.
        if (mapFragment == null) {
            mapFragment = new MidipileSupportMapFragment();
            mapFragment.setMapCallback(this); // This activity will receive the Map object once the map fragment is fully loaded

            Log.d(Constants.TAG, "map frag -=-=-=-=-=-");
            // Then we add it using a FragmentTransaction.
            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();

            fragmentTransaction.add(R.id.map, mapFragment, "dealMap");

            fragmentTransaction.commit();
        }
        else
        {
            mapFragment.setMapCallback(this); // This activity will receive the Map object once the map fragment is fully loaded
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        deal = ((HomeActivity)getActivity()).getSelectedDeal();

    }


    public static void setInsets(Activity context, GoogleMap mMap2) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        mMap2.setPadding(0, config.getPixelInsetTop(true) + config.getNavigationBarHeight(), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }

    @Override
    public void onGoogleMapCreated() {

        android.support.v4.app.FragmentManager fm = getFragmentManager();

        SupportMapFragment mapTemp = (SupportMapFragment) fm.findFragmentById(R.id.map);

        Log.i(Constants.TAG, mapTemp.getClass().toString());

        map = mapTemp.getMap();

        setInsets(getActivity(), map);
    }
}

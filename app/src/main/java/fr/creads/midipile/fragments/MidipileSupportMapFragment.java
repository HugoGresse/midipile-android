package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Author : Hugo Gresse
 * Date : 05/09/14
 */
public class MidipileSupportMapFragment extends SupportMapFragment{

    private onGoogleMapLoadedListener googleMapCallback;



    public interface onGoogleMapLoadedListener {
        public void onGoogleMapCreated();
    }


    public void setMapCallback(onGoogleMapLoadedListener callback)
    {
        this.googleMapCallback = callback;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        googleMapCallback.onGoogleMapCreated();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}

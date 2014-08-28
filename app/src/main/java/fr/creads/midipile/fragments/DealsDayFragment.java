package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.creads.midipile.R;
import fr.creads.midipile.adapters.DealsAdapter;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class DealsDayFragment extends Fragment {

    private ListView dealsListView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("fr.creads.midipile", "dealsDay create view  ======");
        View rootView = inflater.inflate(R.layout.fragment_dealsday, container, false);
        dealsListView = (ListView) rootView.findViewById(R.id.listDealsDayView);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setDealsAdapters(ArrayList<Deal> deals){
        dealsListView.setAdapter(new DealsAdapter(getActivity().getApplicationContext(), deals));
    }
}

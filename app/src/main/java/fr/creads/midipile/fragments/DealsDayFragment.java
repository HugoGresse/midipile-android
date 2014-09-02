package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

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
    private LinearLayout listDealsContainer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("fr.creads.midipile", "dealsDay create view  ======");
        View rootView = inflater.inflate(R.layout.fragment_dealsday, container, false);
        listDealsContainer = (LinearLayout)rootView.findViewById(R.id.listDealsContainer);
        dealsListView = (ListView) rootView.findViewById(R.id.listDealsDayView);

        setInsets(getActivity(), listDealsContainer);

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

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
}

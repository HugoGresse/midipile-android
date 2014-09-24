package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.WhishlistAdapter;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class WhishlistFragment extends Fragment{

    private static final String SCREEN_NAME = "Whishlist";

    private RelativeLayout emptyWhishlistRelativeLayout;
    private Button loginButton;
    private ListView whishList;

    private ArrayList<Deal> dealsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whishlist, container, false);


        whishList = (ListView) rootView.findViewById(R.id.listWhishlist);
        emptyWhishlistRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.emptyWhishlistRelativeLayout);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        setInsets(getActivity(), whishList);

        return rootView;
    }

    @Override
    public void onResume (){
        super.onResume();
        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((HomeActivity)getActivity()).getWhishlist() != null){
            setAdapters((ArrayList<Deal>) ((HomeActivity)getActivity()).getWhishlist());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).openLogin();
            }
        });
    }

    public void setAdapters(ArrayList<Deal> deals){

        if(deals != null && deals.isEmpty()){
            emptyWhishlistRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            emptyWhishlistRelativeLayout.setVisibility(View.GONE);
            dealsList = deals;
            whishList.setAdapter(new WhishlistAdapter(getActivity().getApplicationContext(), dealsList));
        }
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getPixelInsetBottom());
    }
}

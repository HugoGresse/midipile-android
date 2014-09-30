package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.adapters.WhishlistAdapter;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class WishlistFragment extends Fragment{

    private static final String SCREEN_NAME = "Whishlist";

    private RelativeLayout emptyWhishlistRelativeLayout;
    private Button loginButton;
    private ListView whishList;
    private ProgressBar loaderProgressBar;

    private ArrayList<Deal> dealsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whishlist, container, false);

        whishList = (ListView) rootView.findViewById(R.id.listWhishlist);
        emptyWhishlistRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.emptyWhishlistRelativeLayout);
        loaderProgressBar = (ProgressBar) rootView.findViewById(R.id.loaderProgressBar);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        setInsets(getActivity(), whishList);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(((HomeActivity)getActivity()).getUser() != null){

            if( ((HomeActivity)getActivity()).getWhishlist() == null){
                emptyWhishlistRelativeLayout.setVisibility(View.GONE);
                loaderProgressBar.setVisibility(View.VISIBLE);
                ((HomeActivity)getActivity()).loadWhishList();
            } else {
                emptyWhishlistRelativeLayout.setVisibility(View.GONE);
                loaderProgressBar.setVisibility(View.GONE);
                setAdapters((ArrayList<Deal>) ((HomeActivity)getActivity()).getWhishlist());
            }

        } else {
            emptyWhishlistRelativeLayout.setVisibility(View.VISIBLE);
            setAdapters(new ArrayList<Deal>());
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((HomeActivity)getActivity()).getWhishlist() != null){
            dealsList = (ArrayList<Deal>) ((HomeActivity)getActivity()).getWhishlist();
            setAdapters(dealsList);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args2=new Bundle();
                args2.putInt("whishlist", 1);
                Fragment homeFragWishlist = new HomeFragment();
                homeFragWishlist.setArguments(args2);
                ((HomeActivity)getActivity()).openLogin(homeFragWishlist);
            }
        });
    }

    public void setAdapters(ArrayList<Deal> deals){
        dealsList = deals;

        if(deals != null && deals.isEmpty()){
            emptyWhishlistRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            emptyWhishlistRelativeLayout.setVisibility(View.GONE);
            loaderProgressBar.setVisibility(View.GONE);
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

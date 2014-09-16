package fr.creads.midipile.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import fr.creads.midipile.R;

/**
 * Author : Hugo Gresse
 * Date : 16/09/14
 */
public class UserAdressFragment extends Fragment {

    private OnUserUpdateListener mUserUpdateCallback;

    public interface OnUserUpdateListener {
        public void onUserSave();
    }


    private ScrollView adressScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_adress, container, false);
        adressScrollView = (ScrollView) rootView.findViewById(R.id.loginScrollView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setInsets(getActivity(), adressScrollView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mUserUpdateCallback = (OnUserUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserUpdateListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getPixelInsetBottom());
    }

}

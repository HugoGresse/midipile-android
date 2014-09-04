package fr.creads.midipile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealBrandFragment extends Fragment {

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;


    private Deal deal;

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button fbButton;
    private Button twButton;
    private Button httpButton;
    private WebView productWebView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deal_brand, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.brandImageView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.dealProgressBar);

        productWebView = (WebView) rootView.findViewById(R.id.productWebView);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deal = ((HomeActivity)getActivity()).getSelectedDeal();

        productWebView.loadData(deal.getDescriptionSociete(), "text/html", "utf-8");

        if(!deal.getImages().isEmpty()){
            String url = deal.getImages().get(0);
            imageLoader.displayImage(url, imageView, imageLoaderDisplayOptions);
        }

    }


}

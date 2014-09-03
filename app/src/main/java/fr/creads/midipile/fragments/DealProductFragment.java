package fr.creads.midipile.fragments;

import android.graphics.Typeface;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealProductFragment extends Fragment {

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    private Deal deal;

    private Typeface typefaceLoto;

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button participateButton;
    private WebView detailWebView;

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

        View rootView = inflater.inflate(R.layout.fragment_deal_product, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.dealPictureView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.dealProgressBar);
        participateButton = (Button) rootView.findViewById(R.id.participateButton);
        detailWebView = (WebView) rootView.findViewById(R.id.dealDetail);

        typefaceLoto = Typeface.createFromAsset( getActivity().getApplicationContext().getAssets(),
                "fonts/latoregular.ttf");

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deal = ((HomeActivity)getActivity()).getSelectedDeal();

        progressBar.setProgress(getProgress(deal.getDateLancement(), deal.getDateFin()));
        detailWebView.loadData(deal.getDescription(), "text/html", "utf-8");

        if(!deal.getImages().isEmpty()){
            String url = deal.getImages().get(0);
            imageLoader.displayImage(url, imageView, imageLoaderDisplayOptions);
        }

        participateButton.setTypeface(typefaceLoto);

    }

    public int getProgress(String dateLancement, String dateFin){
        SimpleDateFormat mySQLDateFormatter = new SimpleDateFormat( "yyyy-MM-dd", java.util.Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        Date curDate = (Date) cal.getTime();

        Date startDate = null;
        Date endDate = null;
        int progress = 0;
        try {
            startDate = mySQLDateFormatter.parse(dateLancement);
            endDate = mySQLDateFormatter.parse(dateFin);

            int dealDayDuration = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            int curDayDuration = (int) ((curDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));

            progress = (100 * curDayDuration) / dealDayDuration;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return progress;
    }

}
